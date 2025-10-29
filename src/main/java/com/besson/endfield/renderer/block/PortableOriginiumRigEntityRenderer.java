package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.PortableOriginiumRigBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PortableOriginiumRigEntityRenderer implements BlockEntityRenderer<PortableOriginiumRigBlockEntity> {
    private final PortableOriginiumRigRenderer geckoRenderer;

    public PortableOriginiumRigEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.geckoRenderer = new PortableOriginiumRigRenderer(context);
    }

    @Override
    public boolean shouldRenderOffScreen(PortableOriginiumRigBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public void render(PortableOriginiumRigBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        geckoRenderer.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);

        if (!entity.isWorking()) {
            matrices.pushPose();
            matrices.translate(0.5, 2.5, 0.5);

            Minecraft mc = Minecraft.getInstance();
            matrices.mulPose(Axis.YP.rotationDegrees(-mc.player.getYRot()));
            matrices.mulPose(Axis.XP.rotationDegrees(mc.player.getXRot()));
            matrices.scale(0.5f, 0.5f, 0.5f);

            mc.getItemRenderer().renderStatic(
                    new ItemStack(Items.BARRIER),
                    ItemDisplayContext.GUI,
                    light, overlay,
                    matrices, vertexConsumers,
                    mc.level, 0
            );

            matrices.popPose();
        }
    }
}
