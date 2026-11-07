package com.besson.endfield.recipe.custom;

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

public class PlantingUnitRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;
    private final ResourceLocation requiredFluid;
    private final int requiredFluidAmount;

    public PlantingUnitRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this(id, input, output, null, 0);
    }

    public PlantingUnitRecipe(ResourceLocation id, Ingredient input, ItemStack output,
                              ResourceLocation requiredFluid, int requiredFluidAmount) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.requiredFluid = requiredFluid;
        this.requiredFluidAmount = requiredFluidAmount;
    }

    public ResourceLocation getRequiredFluid() {
        return requiredFluid;
    }

    public int getRequiredFluidAmount() {
        return requiredFluidAmount;
    }
    
    public boolean requiresFluid() {
        return requiredFluid != null && requiredFluidAmount > 0;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide()) return false;
        return input.test(inventory.getItem(0));
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
        ingredients.add(input);
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

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Ingredient getInput() {
        return input;
    }

    public static class Type implements RecipeType<PlantingUnitRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "planting_unit";
    }

    public static class Serializer implements RecipeSerializer<PlantingUnitRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "planting_unit";

        @Override
        public PlantingUnitRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            
            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (json.has("fluid")) {
                JsonObject fluidObj = GsonHelper.getAsJsonObject(json, "fluid");
                requiredFluid = new ResourceLocation(GsonHelper.getAsString(fluidObj, "id"));
                requiredFluidAmount = fluidObj.has("amount") ? GsonHelper.getAsInt(fluidObj, "amount") : 1000;
            }

            return new PlantingUnitRecipe(id, input, output, requiredFluid, requiredFluidAmount);
        }

        @Override
        public PlantingUnitRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();

            boolean hasFluid = buf.readBoolean();
            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (hasFluid) {
                requiredFluid = buf.readResourceLocation();
                requiredFluidAmount = buf.readInt();
            }

            return new PlantingUnitRecipe(id, input, output, requiredFluid, requiredFluidAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PlantingUnitRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.output);

            boolean hasFluid = recipe.requiresFluid();
            buf.writeBoolean(hasFluid);
            if (hasFluid) {
                buf.writeResourceLocation(recipe.requiredFluid);
                buf.writeInt(recipe.requiredFluidAmount);
            }
        }
    }
}
