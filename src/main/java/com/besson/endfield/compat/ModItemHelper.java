package com.besson.endfield.compat;

import com.besson.endfield.recipe.InputEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class ModItemHelper {
    public static ItemStack of(ItemLike itemLike, int count) {
        ItemStack stack = new ItemStack(itemLike);
        stack.setCount(count);
        return stack;
    }

    public static List<ItemStack> fromInputEntries(InputEntry entry) {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : entry.getIngredient().getItems()) {
            ItemStack copy = stack.copy();
            copy.setCount(entry.getCount());
            list.add(copy);
        }
        return list;
    }
}
