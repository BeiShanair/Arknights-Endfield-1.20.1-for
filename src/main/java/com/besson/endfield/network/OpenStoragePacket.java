package com.besson.endfield.network;

import com.besson.endfield.screen.custom.logicitis.StorageScreenHandlerFactory;
import com.besson.endfield.util.storage.GlobalStorageManager;
import com.besson.endfield.util.storage.StorageEntry;
import com.besson.endfield.util.storage.StorageState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class OpenStoragePacket {
    public OpenStoragePacket() {}

    public OpenStoragePacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            if (player != null) {
                NetworkHooks.openScreen(
                        player, new StorageScreenHandlerFactory(),
                        buf -> {
                            ServerLevel level = player.serverLevel();
                            StorageState state = GlobalStorageManager.get(level).getState();
                            
                            var entries = state.getStorage();
                            buf.writeInt(entries.size());
                            
                            for (StorageEntry entry : entries.values()) {
                                buf.writeResourceLocation(
                                        ForgeRegistries.ITEMS.getKey(entry.getItem()));
                                buf.writeLong(entry.getCount());
                            }
                        });
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
