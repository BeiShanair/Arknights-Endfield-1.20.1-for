package com.besson.endfield.network;

import com.besson.endfield.screen.custom.logicitis.StorageScreenHandler;
import com.besson.endfield.util.storage.StorageEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * 服务端 → 客户端：整表下发全局仓储内容（含每条的 NBT 变体）。
 */
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
            Item item = ForgeRegistries.ITEMS.getValue(itemId);
            CompoundTag tag = buf.readNbt();
            long count = buf.readLong();

            if (item == null) continue;

            entries.add(new StorageEntry(item, tag, count, 0));
        }
        return new SyncStoragePacket(entries);
    }

    public static void encode(SyncStoragePacket msg, FriendlyByteBuf buf) {
        // 先筛掉物品已失效的条目，保证写入的条数与 readInt 的数量一致
        List<StorageEntry> valid = new ArrayList<>();
        for (StorageEntry entry : msg.entries) {
            if (BuiltInRegistries.ITEM.getKey(entry.getItem()) != null) {
                valid.add(entry);
            }
        }

        buf.writeInt(valid.size());

        for (StorageEntry entry : valid) {
            buf.writeResourceLocation(
                    BuiltInRegistries.ITEM.getKey(entry.getItem())
            );
            buf.writeNbt(entry.getTag());
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
