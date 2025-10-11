package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.ElectricPylonBlockEntity;
import com.besson.endfield.blockEntity.custom.ProtocolAnchorCoreBlockEntity;
import com.besson.endfield.blockEntity.custom.RelayTowerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class ElectricPylonEntityRenderer implements BlockEntityRenderer<ElectricPylonBlockEntity> {
    private final ElectricPylonRenderer geckoRenderer;

    public ElectricPylonEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.geckoRenderer = new ElectricPylonRenderer(context);
    }
    @Override
    public void render(ElectricPylonBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        geckoRenderer.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);

        if (entity.getConnectedNode() == null) return;

        BlockPos pos = entity.getBlockPos().offset(0, 7, 0);

        BlockPos connectedPos = null;
        BlockEntity be;
        if (entity.getLevel() != null) {
            be = entity.getLevel().getBlockEntity(entity.getConnectedNode());
            if (be instanceof RelayTowerBlockEntity) {
                connectedPos = entity.getConnectedNode().offset(0, 10, 0);
            } else if (be instanceof ProtocolAnchorCoreBlockEntity){
                connectedPos = entity.getConnectedNode().offset(0, 27, 0);
            }
        }

        if (connectedPos == null) return;

        Vec3 start = Vec3.atCenterOf(pos).subtract(Vec3.atLowerCornerOf(entity.getBlockPos()));
        Vec3 end = Vec3.atCenterOf(connectedPos).subtract(Vec3.atLowerCornerOf(entity.getBlockPos()));

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderType.lines());

        Matrix4f matrix4f = matrices.last().pose();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double dz = end.z - start.z;

        // 方向向量
        Vec3 dir = new Vec3(dx, dy, dz).normalize();

        // 视角向量（从线起点指向相机）
        Vec3 toCam = cameraPos.subtract(start).normalize();

        // normal = dir × toCam
        Vec3 normal = dir.cross(toCam).normalize();

        consumer.vertex(matrix4f, (float) start.x, (float) start.y, (float) start.z)
                .color(241, 237, 184, 255)
                .normal((float) normal.x, (float) normal.y, (float) normal.z)
                .endVertex();
        consumer.vertex(matrix4f, (float) end.x, (float) end.y, (float) end.z)
                .color(241, 237, 184, 255)
                .normal((float) normal.x, (float) normal.y, (float) normal.z)
                .endVertex();
    }
}
