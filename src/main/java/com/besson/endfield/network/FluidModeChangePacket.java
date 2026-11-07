package com.besson.endfield.network;

import com.besson.endfield.blockEntity.custom.production1.PlantingUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.production1.RefiningUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.FillingUnitBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidModeChangePacket {
    private final BlockPos pos;

    public FluidModeChangePacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(FluidModeChangePacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static FluidModeChangePacket decode(FriendlyByteBuf buf) {
        return new FluidModeChangePacket(buf.readBlockPos());
    }

    public static void handle(FluidModeChangePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof RefiningUnitBlockEntity refiningUnit) {
                refiningUnit.changeFluidMode(player.level());
                player.displayClientMessage(refiningUnit.getFluidMode(), true);
            } else if (be instanceof FillingUnitBlockEntity fillingUnit) {
                fillingUnit.changeFluidMode(player.level(), player);
                player.displayClientMessage(fillingUnit.getFluidMode(), true);
            } else if (be instanceof PlantingUnitBlockEntity plantingUnit) {
                plantingUnit.changeFluidMode(player.level(), player);
                player.displayClientMessage(plantingUnit.getFluidMode(), true);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
