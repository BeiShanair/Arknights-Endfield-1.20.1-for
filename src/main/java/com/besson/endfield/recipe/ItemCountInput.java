package com.besson.endfield.recipe;

import net.minecraft.world.level.ItemLike;

public class ItemCountInput {
    private final ItemLike itemConvertible;
    private final int count;

    public ItemCountInput(ItemLike itemConvertible, int count) {
        this.itemConvertible = itemConvertible;
        this.count = count;
    }

    public ItemLike getItemConvertible() {
        return itemConvertible;
    }

    public int getCount() {
        return count;
    }
}
