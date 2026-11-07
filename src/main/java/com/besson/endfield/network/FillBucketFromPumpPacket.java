package com.besson.endfield.network;

import com.besson.endfield.blockEntity.custom.BucketFillable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FillBucketFromPumpPacket {
    private final BlockPos pos;

    public FillBucketFromPumpPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(FillBucketFromPumpPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static FillBucketFromPumpPacket decode(FriendlyByteBuf buf) {
        return new FillBucketFromPumpPacket(buf.readBlockPos());
    }

    public static void handle(FillBucketFromPumpPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof BucketFillable fillable) {
                fillable.tryFillBucket(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
