package com.besson.endfield.renderer.block;

import com.besson.endfield.block.custom.logicitis.BeltBlock;
import com.besson.endfield.block.custom.logicitis.BeltShape;
import com.besson.endfield.blockEntity.custom.logicitis.BeltBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BeltRenderer implements BlockEntityRenderer<BeltBlockEntity> {

    public BeltRenderer(BlockEntityRendererProvider.Context context){

    }

    @Override
    public void render(BeltBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemStack stack = entity.getStoredItem();

        if (stack.isEmpty()) return;

        float progress = entity.progress;

        matrices.pushPose();

        applyTransform(entity, progress, matrices);
        renderItem(stack, matrices, vertexConsumers, light, overlay);

        matrices.popPose();
    }
    
    private void applyTransform(BeltBlockEntity be, float progress, PoseStack matrices) {

        BlockState state = be.getBlockState();
        BeltShape shape = state.getValue(BeltBlock.SHAPE);

        Direction from = be.getTravelDirection();
        if (from == null) return;

        Direction to = BeltBlock.getNextDirection(shape, from);
        if (to == null) return;

        // 起点偏移
        Vec3 start = getOffsetVec(from, 0.5f);

        // 终点偏移
        Vec3 end = getOffsetVec(to, 0.5f);

        // 上坡处理
        if (BeltBlock.isAscendingTowards(shape, to)) {
            end = new Vec3(end.x(), 1f, end.z());
        }
        if (BeltBlock.isAscendingTowards(shape, from)) {
            start = new Vec3(start.x(), 1f, start.z());
        }
        // 插值
        float x = lerp(progress, (float) start.x(), (float) end.x());
        float y = lerp(progress, (float) start.y(), (float) end.y());
        float z = lerp(progress, (float) start.z(), (float) end.z());
        
        matrices.translate(0.5f + x, 0.07f + y, 0.5f + z);
        matrices.scale(0.4f, 0.4f, 0.4f);
    }

    private float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    private Vec3 getOffsetVec(Direction dir, float distance) {

        return switch (dir) {
            case NORTH -> new Vec3(0, 0, -distance);
            case SOUTH -> new Vec3(0, 0, distance);
            case WEST  -> new Vec3(-distance, 0, 0);
            case EAST  -> new Vec3(distance, 0, 0);
            default -> new Vec3(0, 0, 0);
        };
    }
    
    private void renderItem(ItemStack stack, PoseStack matrices, MultiBufferSource vertices, int light, int overlay) {

        matrices.mulPose(Axis.XP.rotationDegrees(90));

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, matrices, vertices, null, 0);
    }
}
