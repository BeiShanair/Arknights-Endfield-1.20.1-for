package com.besson.endfield.network;

import com.besson.endfield.screen.custom.logicitis.StorageScreenHandler;
import com.besson.endfield.util.storage.StorageEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SyncStoragePacket {
    private final List<StorageEntry> entries;

    public SyncStoragePacket(Collection<StorageEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    public static SyncStoragePacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<StorageEntry> entries = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            var itemId = buf.readResourceLocation();
            var item = ForgeRegistries.ITEMS.getValue(itemId);
            long count = buf.readLong();

            entries.add(new StorageEntry(item, count, 0));
        }
        return new SyncStoragePacket(entries);
    }

    public static void encode(SyncStoragePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entries.size());

        for (StorageEntry entry : msg.entries) {
            buf.writeResourceLocation(
                    BuiltInRegistries.ITEM.getKey(entry.getItem())
            );
            buf.writeLong(entry.getCount());
        }
    }
    
    public static void handle(SyncStoragePacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        if (context.getDirection().getReceptionSide().isClient()) {

            context.enqueueWork(() -> {
                Minecraft client = Minecraft.getInstance();

                if (client.player != null &&
                        client.player.containerMenu instanceof StorageScreenHandler screenHandler) {

                    screenHandler.updateEntries(msg.entries);
                }
            });
        }

        context.setPacketHandled(true);
    }
}
