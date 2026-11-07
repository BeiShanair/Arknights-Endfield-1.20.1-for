package com.besson.endfield.renderer.block.powering;

import com.besson.endfield.blockEntity.custom.powering.BasePylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BaseRelayBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.ProtocolAnchorCoreBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

final class PowerWireRenderer {
    record WirePalette(int endR, int endG, int endB, int midR, int midG, int midB) {
        static final WirePalette ELECTRIC = new WirePalette(241, 237, 184, 140, 200, 255);
        static final WirePalette XIRANITE = new WirePalette(43, 152, 8, 215, 255, 183);
    }

    private PowerWireRenderer() {
    }
    
    static @Nullable BlockPos resolveTargetHeight(@Nullable Level level, BlockPos target) {
        if (level == null) return null;
        BlockEntity connectedEntity = level.getBlockEntity(target);
        if (connectedEntity instanceof BaseRelayBlockEntity) {
            return target.offset(0, 10, 0);
        } else if (connectedEntity instanceof ProtocolAnchorCoreBlockEntity) {
            return target.offset(0, 27, 0);
        } else if (connectedEntity instanceof BasePylonBlockEntity) {
            return target.offset(0, 7, 0);
        }
        return null;
    }
    
    static void render(PoseStack matrices, MultiBufferSource buffers, Vec3 start, Vec3 end, WirePalette palette) {
        Vec3 delta = end.subtract(start);
        if (delta.lengthSqr() < 1.0E-16) return;
        
        Vec3 dir = delta.normalize();
        float nx = (float) dir.x;
        float ny = (float) dir.y;
        float nz = (float) dir.z;

        VertexConsumer consumer = buffers.getBuffer(RenderType.lines());
        Matrix4f matrix = matrices.last().pose();
        Vec3 mid = start.add(delta.scale(0.5));

        vertex(consumer, matrix, start, palette.endR(), palette.endG(), palette.endB(), nx, ny, nz);
        vertex(consumer, matrix, mid, palette.midR(), palette.midG(), palette.midB(), nx, ny, nz);
        vertex(consumer, matrix, mid, palette.midR(), palette.midG(), palette.midB(), nx, ny, nz);
        vertex(consumer, matrix, end, palette.endR(), palette.endG(), palette.endB(), nx, ny, nz);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, Vec3 pos,
                               int r, int g, int b, float nx, float ny, float nz) {
        consumer.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z)
                .color(r, g, b, 255)
                .normal(nx, ny, nz)
                .endVertex();
    }
}
