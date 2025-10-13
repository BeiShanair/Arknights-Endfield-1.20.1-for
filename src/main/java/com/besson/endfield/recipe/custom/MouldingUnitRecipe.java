package com.besson.endfield.recipe.custom;

import com.besson.endfield.recipe.InputEntry;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class MouldingUnitRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final InputEntry input;
    private final ItemStack output;

    public MouldingUnitRecipe(ResourceLocation id, InputEntry input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide()) return false;
        ItemStack inputs = inventory.getItem(0);
        return input.getIngredient().test(inputs);
    }

    public InputEntry getInput() {
        return input;
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryManager) {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input.getIngredient());
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<MouldingUnitRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "moulding_unit";
    }

    public static class Serializer implements RecipeSerializer<MouldingUnitRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "moulding_unit";

        @Override
        public MouldingUnitRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject ingredients = GsonHelper.getAsJsonObject(json, "input");
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            InputEntry inputs;

            Ingredient ingredient = Ingredient.fromJson(ingredients);
            int count = ingredients.has("count") ? GsonHelper.getAsInt(ingredients, "count") : 1;
            inputs = new InputEntry(ingredient, count);

            return new MouldingUnitRecipe(id, inputs, output);
        }

        @Override
        public MouldingUnitRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int count = buf.readInt();
            InputEntry inputs = new InputEntry(ingredient, count);
            ItemStack output = buf.readItem();
            return new MouldingUnitRecipe(id, inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MouldingUnitRecipe recipe) {
            InputEntry entry = recipe.getInput();
            entry.getIngredient().toNetwork(buf);
            buf.writeInt(entry.getCount());
            buf.writeItem(recipe.output);
        }
    }
}
