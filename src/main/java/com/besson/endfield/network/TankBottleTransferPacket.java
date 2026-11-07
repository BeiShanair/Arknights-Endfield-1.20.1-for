package com.besson.endfield.network;

import com.besson.endfield.blockEntity.custom.depot.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TankBottleTransferPacket {
    private final BlockPos pos;

    public TankBottleTransferPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(TankBottleTransferPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static TankBottleTransferPacket decode(FriendlyByteBuf buf) {
        return new TankBottleTransferPacket(buf.readBlockPos());
    }

    public static void handle(TankBottleTransferPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof FluidTankBlockEntity tank) {
                tank.tryTransferWithBottle(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
