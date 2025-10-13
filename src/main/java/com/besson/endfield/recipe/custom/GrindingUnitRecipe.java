package com.besson.endfield.recipe.custom;

import com.besson.endfield.recipe.InputEntry;
import com.google.gson.JsonArray;
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

import java.util.ArrayList;
import java.util.List;

public class GrindingUnitRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<InputEntry> input;
    private final ItemStack output;

    public GrindingUnitRecipe(ResourceLocation id, NonNullList<InputEntry> input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide) return false;

        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inputs.add(inventory.getItem(i));
        }

        for (InputEntry inputEntry : input) {
            boolean matched = false;
            for (ItemStack stack : inputs) {
                if (inputEntry.getIngredient().test(stack)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }
        return true;
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
        for (InputEntry entry : input) {
            ingredients.add(entry.getIngredient());
        }
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

    public NonNullList<InputEntry> getInput() {
        return input;
    }

    public static class Type implements RecipeType<GrindingUnitRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "grinding_unit";
    }

    public static class Serializer implements RecipeSerializer<GrindingUnitRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "grinding_unit";

        @Override
        public GrindingUnitRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "input");
            NonNullList<InputEntry> inputs = NonNullList.withSize(ingredients.size(), InputEntry.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                JsonObject obj = ingredients.get(i).getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(obj);
                int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
                inputs.set(i, new InputEntry(ingredient, count));
            }

            return new GrindingUnitRecipe(id, inputs, output);
        }

        @Override
        public GrindingUnitRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<InputEntry> inputs = NonNullList.withSize(buf.readInt(), InputEntry.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                Ingredient ingredient = Ingredient.fromNetwork(buf);
                int count = buf.readInt();
                inputs.set(i, new InputEntry(ingredient, count));
            }
            ItemStack output = buf.readItem();
            return new GrindingUnitRecipe(id, inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GrindingUnitRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (InputEntry entry: recipe.input) {
                entry.getIngredient().toNetwork(buf);
                buf.writeInt(entry.getCount());
            }
            buf.writeItem(recipe.output);
        }
    }
}
