package com.besson.endfield.util.storage;

import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.network.SyncStoragePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

public class GlobalStorageManager {
    private static final Map<ServerLevel, GlobalStorageManager> INSTANCE = new WeakHashMap<>();
    private final ServerLevel world;
    private final StorageState state;
    private final Set<ServerPlayer> listeners = new HashSet<>();

    public GlobalStorageManager(ServerLevel world) {
        this.world = world;
        DimensionDataStorage manager = world.getDataStorage();

        String dimKey = world.dimension().location().toString().replace(':', '_').replace('/', '_');
        String stateName = "storage_state_" + dimKey;

        this.state = manager.computeIfAbsent(StorageState::fromNbt, StorageState::new, stateName);
    }

    public static GlobalStorageManager get(ServerLevel world) {
        return INSTANCE.computeIfAbsent(world, GlobalStorageManager::new);
    }

    public void addListener(ServerPlayer player) {
        listeners.add(player);
    }

    public void removeListener(ServerPlayer player) {
        listeners.remove(player);
    }
    
    public long insert(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        CompoundTag tag = StorageEntry.normalizeTag(stack.getTag());
        long free = getFreeSpace(stack.getItem(), tag);
        if (free <= 0) return 0;

        long amount = Math.min(stack.getCount(), free);
        StorageEntry entry = state.getOrCreate(stack.getItem(), tag);
        long inserted = entry.insert(amount);
        if (inserted <= 0) return 0;

        state.setDirty();
        sync();
        return inserted;
    }
    
    public long getFreeSpace(Item item, @Nullable CompoundTag tag) {
        CompoundTag normalized = StorageEntry.normalizeTag(tag);
        StorageEntry entry = state.find(item, normalized);
        long capacity = entry == null ? state.getGlobalCapacity() : entry.getCapacity();
        long count = entry == null ? 0 : entry.getCount();
        return Math.max(0, capacity - count);
    }
    
    public ItemStack extract(Item item, long amount) {
        StorageEntry entry = state.findFirst(item);
        if (entry == null) return ItemStack.EMPTY;
        return extractFrom(entry, amount);
    }
    
    public ItemStack extract(ItemStack prototype, long amount) {
        if (prototype == null || prototype.isEmpty()) return ItemStack.EMPTY;
        StorageEntry entry = state.find(prototype.getItem(), prototype.getTag());
        if (entry == null) return ItemStack.EMPTY;
        return extractFrom(entry, amount);
    }

    private ItemStack extractFrom(StorageEntry entry, long amount) {
        if (amount <= 0) return ItemStack.EMPTY;
        long max = Math.max(1, entry.getItem().getMaxStackSize());
        long toTake = Math.min(amount, max);
        long taken = entry.extract(toTake);
        if (taken <= 0) return ItemStack.EMPTY;

        if (entry.getCount() <= 0) {
            state.removeEntry(entry);
        }
        state.setDirty();
        sync();
        return entry.toStack((int) taken);
    }
    
    public static int removeFromInventory(Inventory inv, Item item, @Nullable CompoundTag tag, int amount) {
        if (inv == null || item == null || amount <= 0) return 0;
        CompoundTag normalized = StorageEntry.normalizeTag(tag);
        int removed = 0;
        for (int i = 0; i < inv.getContainerSize() && removed < amount; i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item
                    && Objects.equals(StorageEntry.normalizeTag(stack.getTag()), normalized)) {
                int take = Math.min(stack.getCount(), amount - removed);
                stack.shrink(take);
                if (stack.isEmpty()) inv.setItem(i, ItemStack.EMPTY);
                removed += take;
            }
        }
        if (removed > 0) inv.setChanged();
        return removed;
    }

    private void sync() {
        for (ServerPlayer player : listeners) {
            sendFullUpdate(player);
        }
    }

    private void sendFullUpdate(ServerPlayer player) {
        ModNetWorking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncStoragePacket(state.getEntries())
        );
    }

    public ServerLevel getWorld() {
        return world;
    }

    public StorageState getState() {
        return state;
    }
}
