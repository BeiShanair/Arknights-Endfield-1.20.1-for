package com.besson.endfield.util.storage;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class StorageState extends SavedData {

    private Map<Item, StorageEntry> storage = new HashMap<>();
    private long globalCapacity = 10000;

    public StorageState() {

    }

    public static StorageState fromNbt(CompoundTag nbt) {
        StorageState state = new StorageState();

        ListTag list = nbt.getList("storage", Tag.TAG_COMPOUND);

        for (Tag element : list) {
            CompoundTag tag = (CompoundTag) element;

            Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(tag.getString("id")));
            long count = tag.getLong("count");
            long cap = tag.getLong("cap");

            StorageEntry entry = new StorageEntry(item, cap);
            entry.insert(count);

            state.storage.put(item, entry);
        }
        state.globalCapacity = nbt.getLong("globalCapacity");
        if (state.globalCapacity == 0) {
            state.globalCapacity = 10000;
        }
        return state;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();

        for (StorageEntry entry : storage.values()) {
            CompoundTag tag = new CompoundTag();

            tag.putString("id", ForgeRegistries.ITEMS.getKey(entry.getItem()).toString());
            tag.putLong("count", entry.getCount());
            tag.putLong("cap", entry.getCapacity());

            list.add(tag);
        }

        nbt.put("storage", list);
        nbt.putLong("globalCapacity", globalCapacity);
        return nbt;
    }

    public Map<Item, StorageEntry> getStorage() {
        return storage;
    }

    public long getGlobalCapacity() {
        return globalCapacity;
    }

    public void setGlobalCapacity(long cap) {
        this.globalCapacity = cap;
        for (StorageEntry entry : storage.values()) {
            entry.setCapacity(cap);
        }
        setDirty();
    }
}
