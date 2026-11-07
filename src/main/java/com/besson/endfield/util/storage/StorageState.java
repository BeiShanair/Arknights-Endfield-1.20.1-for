package com.besson.endfield.util.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StorageState extends SavedData {

    private final List<StorageEntry> storage = new ArrayList<>();
    private long globalCapacity = 10000;

    public StorageState() {
    }

    public static StorageState fromNbt(CompoundTag nbt) {
        StorageState state = new StorageState();

        ListTag list = nbt.getList("storage", Tag.TAG_COMPOUND);

        for (Tag element : list) {
            CompoundTag tag = (CompoundTag) element;

            Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(tag.getString("id")));
            if (item == null) {
                continue;
            }

            long count = tag.getLong("count");
            long cap = tag.getLong("cap");
            CompoundTag variantTag = tag.contains("tag", Tag.TAG_COMPOUND)
                    ? tag.getCompound("tag") : null;

            state.storage.add(new StorageEntry(item, variantTag, count, cap));
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

        for (StorageEntry entry : storage) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(entry.getItem());
            if (id == null) {
                continue;
            }

            CompoundTag tag = new CompoundTag();
            tag.putString("id", id.toString());
            tag.putLong("count", entry.getCount());
            tag.putLong("cap", entry.getCapacity());

            CompoundTag variantTag = entry.getTag();
            if (variantTag != null) {
                tag.put("tag", variantTag);
            }

            list.add(tag);
        }

        nbt.put("storage", list);
        nbt.putLong("globalCapacity", globalCapacity);
        return nbt;
    }

    public List<StorageEntry> getEntries() {
        return storage;
    }
    
    @Nullable
    public StorageEntry find(Item item, @Nullable CompoundTag tag) {
        CompoundTag normalized = StorageEntry.normalizeTag(tag);
        for (StorageEntry entry : storage) {
            if (entry.matches(item, normalized)) {
                return entry;
            }
        }
        return null;
    }
    
    @Nullable
    public StorageEntry findFirst(Item item) {
        for (StorageEntry entry : storage) {
            if (entry.isAnyOf(item)) {
                return entry;
            }
        }
        return null;
    }

    public StorageEntry getOrCreate(Item item, @Nullable CompoundTag tag) {
        CompoundTag normalized = StorageEntry.normalizeTag(tag);
        StorageEntry found = find(item, normalized);
        if (found != null) {
            return found;
        }
        StorageEntry entry = new StorageEntry(item, normalized, 0, globalCapacity);
        storage.add(entry);
        return entry;
    }

    public void removeEntry(StorageEntry entry) {
        if (storage.remove(entry)) {
            setDirty();
        }
    }

    public long getGlobalCapacity() {
        return globalCapacity;
    }

    public void setGlobalCapacity(long cap) {
        this.globalCapacity = cap;
        for (StorageEntry entry : storage) {
            entry.setCapacity(cap);
        }
        setDirty();
    }
}
