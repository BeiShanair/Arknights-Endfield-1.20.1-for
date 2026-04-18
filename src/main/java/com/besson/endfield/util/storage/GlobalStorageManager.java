package com.besson.endfield.util.storage;

import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.network.SyncStoragePacket;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Map;
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

    public void removeListener(ServerPlayer serverPlayer) {
        listeners.remove(serverPlayer);
    }
    
    public long insert(ItemStack stack) {
        Map<Item, StorageEntry> storageEntryMap = state.getStorage();

        StorageEntry entry = storageEntryMap.computeIfAbsent(
                stack.getItem(),
                item -> new StorageEntry(item, state.getGlobalCapacity()));
        long inserted = entry.insert(stack.getCount());
        state.setDirty();
        sync();
        return inserted;
    }

    public ItemStack extract(Item item, long amount) {
        StorageEntry entry = state.getStorage().get(item);
        if (entry == null) return ItemStack.EMPTY;
    
        long taken = entry.extract(amount);

        if (entry.getCount() <= 0) {
            state.getStorage().remove(item);
        }
    
        state.setDirty();
        sync();
        return new ItemStack(item, (int) Math.min(taken, item.getMaxStackSize()));
    }

    private void sync() {
        for (ServerPlayer player : listeners) {
            sendFullUpdate(player);
        }
    }

    private void sendFullUpdate(ServerPlayer player) {
        ModNetWorking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncStoragePacket(state.getStorage().values())
        );
    }
    
    public ServerLevel getWorld() {
        return world;
    }

    public StorageState getState() {
        return state;
    }
}
