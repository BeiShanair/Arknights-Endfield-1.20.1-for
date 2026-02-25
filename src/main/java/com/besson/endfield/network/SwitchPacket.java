package com.besson.endfield.network;

import com.besson.endfield.blockEntity.custom.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwitchPacket {
    private final BlockPos pos;
    private final boolean enable;

    public SwitchPacket(BlockPos pos, boolean enable) {
        this.pos = pos;
        this.enable = enable;
    }

    public static void encode(SwitchPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeBoolean(msg.enable);
    }

    public static SwitchPacket decode(FriendlyByteBuf buf) {
        return new SwitchPacket(
                buf.readBlockPos(),
                buf.readBoolean()
        );
    }
    
    public static void handle(SwitchPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof BaseRigBlockEntity<?> rig) {
                rig.setEnable(msg.enable);
            }
            if (be instanceof ThermalBankBlockEntity tb) {
                tb.setEnable(msg.enable);
            }
            if (be instanceof BaseIOBlockEntity<?> b) {
                b.setEnable(msg.enable);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
