package com.besson.endfield.util.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StorageEntry {
    private final Item item;
    private final CompoundTag tag;
    private long count;
    private long capacity;

    public StorageEntry(Item item, long capacity) {
        this(item, null, 0, capacity);
    }

    public StorageEntry(Item item, long count, long capacity) {
        this(item, null, count, capacity);
    }

    public StorageEntry(Item item, @Nullable CompoundTag tag, long count, long capacity) {
        this.item = item;
        CompoundTag normalized = normalizeTag(tag);
        this.tag = normalized == null ? null : normalized.copy();
        this.count = count;
        this.capacity = capacity;
    }

    public StorageEntry(StorageEntry other) {
        this(other.item, other.tag, other.count, other.capacity);
    }
    
    @Nullable
    public static CompoundTag normalizeTag(@Nullable CompoundTag tag) {
        if (tag == null || tag.isEmpty()) {
            return null;
        }
        return tag;
    }

    public boolean matches(Item item, @Nullable CompoundTag tag) {
        return this.item == item && Objects.equals(this.tag, normalizeTag(tag));
    }

    public boolean isAnyOf(Item item) {
        return this.item == item;
    }
    
    public ItemStack toStack(int count) {
        ItemStack out = new ItemStack(item, Math.max(0, count));
        if (tag != null) {
            out.setTag(tag.copy());
        }
        return out;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long insert(long amount) {
        long accepted = Math.min(amount, capacity - count);
        count += accepted;
        return accepted;
    }

    public long extract(long amount) {
        long taken = Math.min(amount, count);
        count -= taken;
        return taken;
    }

    public Item getItem() {
        return item;
    }

    @Nullable
    public CompoundTag getTag() {
        return tag == null ? null : tag.copy();
    }

    public long getCapacity() {
        return capacity;
    }

    public long getCount() {
        return count;
    }
}
