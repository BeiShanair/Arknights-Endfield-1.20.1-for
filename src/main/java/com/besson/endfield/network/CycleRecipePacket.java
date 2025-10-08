package com.besson.endfield.network;

import com.besson.endfield.screen.custom.CrafterScreenHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CycleRecipePacket {
    public CycleRecipePacket() {

    }

    public CycleRecipePacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            if (player != null && player.containerMenu instanceof CrafterScreenHandler screenHandler) {
                screenHandler.changeRecipe();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
