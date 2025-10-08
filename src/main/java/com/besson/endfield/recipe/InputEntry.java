package com.besson.endfield.recipe;

import net.minecraft.world.item.crafting.Ingredient;

public class InputEntry {
    public static final InputEntry EMPTY = new InputEntry(Ingredient.EMPTY, 0);
    private final Ingredient ingredient;
    private final int count;

    public InputEntry(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getCount() {
        return count;
    }
}
