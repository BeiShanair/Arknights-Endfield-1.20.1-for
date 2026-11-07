package com.besson.endfield.renderer.block.powering;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ElectricPylonEntityRenderer implements BlockEntityRenderer<ElectricPylonBlockEntity> {
    private static final int WIRE_HEIGHT = 7;

    private final ElectricPylonRenderer geckoRenderer;

    public ElectricPylonEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.geckoRenderer = new ElectricPylonRenderer(context);
    }

    @Override
    public boolean shouldRenderOffScreen(ElectricPylonBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(ElectricPylonBlockEntity entity, float tickDelta, PoseStack matrices,
                       MultiBufferSource buffers, int light, int overlay) {
        geckoRenderer.render(entity, tickDelta, matrices, buffers, light, overlay);

        BlockPos connected = entity.getConnectedNode();
        if (connected == null) return;

        BlockPos connectedPos = PowerWireRenderer.resolveTargetHeight(entity.getLevel(), connected);
        if (connectedPos == null) return;

        Vec3 start = Vec3.atCenterOf(entity.getBlockPos().offset(0, WIRE_HEIGHT, 0))
                .subtract(Vec3.atLowerCornerOf(entity.getBlockPos()));
        Vec3 end = Vec3.atCenterOf(connectedPos)
                .subtract(Vec3.atLowerCornerOf(entity.getBlockPos()));

        PowerWireRenderer.render(matrices, buffers, start, end, PowerWireRenderer.WirePalette.ELECTRIC);
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
