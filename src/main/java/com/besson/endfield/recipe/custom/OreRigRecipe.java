package com.besson.endfield.recipe.custom;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class OreRigRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;
    private final int tier;

    public OreRigRecipe(ResourceLocation id, Ingredient input, ItemStack output, int tier) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide()) return false;
        return input.test(inventory.getItem(0));
    }

    public Ingredient getInput() {
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
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<OreRigRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "ore_rig";
    }

    public static class Serializer implements RecipeSerializer<OreRigRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "ore_rig";

        @Override
        public OreRigRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int tier = GsonHelper.getAsInt(json, "tier");
            return new OreRigRecipe(id, input, output, tier);
        }

        @Override
        public OreRigRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            int tier = buf.readInt();
            return new OreRigRecipe(id, input, output, tier);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, OreRigRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.output);
            buf.writeInt(recipe.tier);
        }
    }
}
