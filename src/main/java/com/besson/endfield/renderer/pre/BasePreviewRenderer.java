package com.besson.endfield.renderer.pre;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public abstract class BasePreviewRenderer<T extends BlockEntity> {
    private T fakeBe = null;

    protected void start() {
        MinecraftForge.EVENT_BUS.addListener(this::renderPreview);
    }

    private void renderPreview(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        ItemStack stack = client.player.getMainHandItem();
        if (!matchesItem(stack)) return;

        BlockHitResult bhr = resolvePlacementHit(client, event);
        if (bhr == null) return;

        BlockPos placePos = bhr.getBlockPos().relative(bhr.getDirection());
        Level level = client.level;
        if (!MultiBlockPlacementUtil.isReplaceable(level, placePos)) return;

        BlockState previewState = getPreviewState(stack, bhr, client);

        fakeBe = createFakeBe(placePos, previewState);
        if (fakeBe == null) return;
        fakeBe.setLevel(level);

        Vec3 cameraPos = event.getCamera().getPosition();
        double camX = cameraPos.x;
        double camY = cameraPos.y;
        double camZ = cameraPos.z;

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(placePos.getX() - camX, placePos.getY() - camY, placePos.getZ() - camZ);

        MultiBufferSource.BufferSource bufferSource = client.renderBuffers().bufferSource();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 0.5f);

        @SuppressWarnings("unchecked")
        BlockEntityRenderer<T> renderer = (BlockEntityRenderer<T>) client.getBlockEntityRenderDispatcher().getRenderer(fakeBe);

        if (renderer != null) {
            renderer.render(fakeBe, event.getPartialTick(), poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
        }

        AABB box = getOutlineBox(previewState);
        if (box != null) {
            VertexConsumer outline = bufferSource.getBuffer(RenderType.lines());
            float[] col = getOutlineColor();
            LevelRenderer.renderLineBox(poseStack, outline, box, col[0], col[1], col[2], col[3]);
        }

        poseStack.popPose();
        bufferSource.endBatch();

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    private BlockHitResult resolvePlacementHit(Minecraft client, RenderLevelStageEvent event) {
        if (client.screen != null && client.screen.isPauseScreen()) return null;
        if (client.mouseHandler.isMouseGrabbed()) {
            return client.hitResult instanceof BlockHitResult blockHitResult ? blockHitResult : null;
        }

        return resolveMouseBlockHit(client, event);
    }

    private BlockHitResult resolveMouseBlockHit(Minecraft client, RenderLevelStageEvent event) {
        Level level = client.level;
        if (level == null) return null;

        Window window = client.getWindow();
        double mouseX = client.mouseHandler.xpos() * window.getWidth() / (double) window.getScreenWidth();
        double mouseY = client.mouseHandler.ypos() * window.getHeight() / (double) window.getScreenHeight();
        float ndcX = (float) (mouseX / window.getWidth() * 2.0D - 1.0D);
        float ndcY = (float) (1.0D - mouseY / window.getHeight() * 2.0D);

        Matrix4f inverseViewProjection = new Matrix4f(event.getProjectionMatrix())
                .mul(event.getPoseStack().last().pose())
                .invert();

        Vector4f near = new Vector4f(ndcX, ndcY, -1.0F, 1.0F).mul(inverseViewProjection);
        Vector4f far = new Vector4f(ndcX, ndcY, 1.0F, 1.0F).mul(inverseViewProjection);
        if (near.w() == 0.0F || far.w() == 0.0F) return null;

        Vec3 nearPoint = new Vec3(near.x() / near.w(), near.y() / near.w(), near.z() / near.w());
        Vec3 farPoint = new Vec3(far.x() / far.w(), far.y() / far.w(), far.z() / far.w());
        Vec3 direction = farPoint.subtract(nearPoint);
        if (direction.lengthSqr() < 1.0E-7D) return null;

        Vec3 start = event.getCamera().getPosition();
        double reach = Math.max(256.0D, client.options.getEffectiveRenderDistance() * 16.0D);
        Vec3 end = start.add(direction.normalize().scale(reach));

        BlockHitResult hit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                event.getCamera().getEntity()
        ));
        return hit.getType() == HitResult.Type.BLOCK ? hit : null;
    }

    protected float[] getOutlineColor() {
        return new float[]{0.3f, 1.0f, 0.3f, 0.4f};
    }

    protected abstract boolean matchesItem(ItemStack stack);

    protected abstract BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client);

    protected abstract T createFakeBe(BlockPos pos, BlockState state);

    protected AABB getOutlineBox(BlockState previewState) {
        double pad1 = 0.002;
        return new AABB(-pad1, -pad1, -pad1, pad1, pad1, pad1);
    }
}
