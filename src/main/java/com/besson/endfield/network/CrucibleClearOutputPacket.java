package com.besson.endfield.network;

import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleBlockEntity;
import com.besson.endfield.screen.custom.production2.BaseCrucibleScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CrucibleClearOutputPacket {
    private final BlockPos pos;
    private final int index;

    public CrucibleClearOutputPacket(BlockPos pos, int index) {
        this.pos = pos;
        this.index = index;
    }

    public static void encode(CrucibleClearOutputPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.index);
    }

    public static CrucibleClearOutputPacket decode(FriendlyByteBuf buf) {
        return new CrucibleClearOutputPacket(buf.readBlockPos(), buf.readInt());
    }

    public static void handle(CrucibleClearOutputPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            if (!(player.containerMenu instanceof BaseCrucibleScreenHandler handler)) return;
            BaseCrucibleBlockEntity crucible = handler.getBlockEntity();
            if (!crucible.getBlockPos().equals(msg.pos)) return;
            crucible.clearOutputFilter(msg.index);
        });
        ctx.get().setPacketHandled(true);
    }
}
