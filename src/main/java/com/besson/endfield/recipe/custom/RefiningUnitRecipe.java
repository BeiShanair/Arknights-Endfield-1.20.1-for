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

public class RefiningUnitRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final InputEntry input;
    private final ItemStack output;
    private final ResourceLocation requiredFluid;
    private final int requiredFluidAmount;
    private final ResourceLocation outputFluid;
    private final int outputFluidAmount;

    public RefiningUnitRecipe(ResourceLocation id, InputEntry input, ItemStack output,
                              ResourceLocation requiredFluid, int requiredFluidAmount,
                              ResourceLocation outputFluid, int outputFluidAmount) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.requiredFluid = requiredFluid;
        this.requiredFluidAmount = requiredFluidAmount;
        this.outputFluid = outputFluid;
        this.outputFluidAmount = outputFluidAmount;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide()) return false;
        ItemStack inputs = inventory.getItem(0);
        return input.ingredient().test(inputs);
    }

    public InputEntry getInput() {
        return input;
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

    public ResourceLocation getOutputFluid() {
        return outputFluid;
    }

    public int getOutputFluidAmount() {
        return outputFluidAmount;
    }

    public boolean producesFluid() {
        return outputFluid != null && outputFluidAmount > 0;
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
        ingredients.add(input.ingredient());
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

    public static class Type implements RecipeType<RefiningUnitRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "refining_unit";
    }

    public static class Serializer implements RecipeSerializer<RefiningUnitRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "refining_unit";

        @Override
        public RefiningUnitRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject ingredients = GsonHelper.getAsJsonObject(json, "input");
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            InputEntry inputs;

            Ingredient ingredient = Ingredient.fromJson(ingredients);
            int count = ingredients.has("count") ? ingredients.get("count").getAsInt() : 1;
            inputs = new InputEntry(ingredient, count);

            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (json.has("fluid")) {
                JsonObject fluidObj = GsonHelper.getAsJsonObject(json, "fluid");
                ResourceLocation fluidId = new ResourceLocation(GsonHelper.getAsString(fluidObj, "id"));
                requiredFluid = fluidId;
                requiredFluidAmount = fluidObj.has("amount") ? fluidObj.get("amount").getAsInt() : 1000;
            }

            ResourceLocation outputFluid = null;
            int outputFluidAmount = 0;
            if (json.has("output_fluid")) {
                JsonObject fluidObj = GsonHelper.getAsJsonObject(json, "output_fluid");
                ResourceLocation fluidId = new ResourceLocation(GsonHelper.getAsString(fluidObj, "id"));
                outputFluid = fluidId;
                outputFluidAmount = fluidObj.has("amount") ? fluidObj.get("amount").getAsInt() : 1000;
            }

            return new RefiningUnitRecipe(id, inputs, output, requiredFluid, requiredFluidAmount, outputFluid, outputFluidAmount);
        }

        @Override
        public RefiningUnitRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int count = buf.readInt();
            InputEntry inputs = new InputEntry(ingredient, count);
            ItemStack output = buf.readItem();

            boolean hasFluid = buf.readBoolean();
            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (hasFluid) {
                requiredFluid = buf.readResourceLocation();
                requiredFluidAmount = buf.readInt();
            }

            boolean hasOutputFluid = buf.readBoolean();
            ResourceLocation outputFluid = null;
            int outputFluidAmount = 0;
            if (hasOutputFluid) {
                outputFluid = buf.readResourceLocation();
                outputFluidAmount = buf.readInt();
            }

            return new RefiningUnitRecipe(id, inputs, output, requiredFluid, requiredFluidAmount, outputFluid, outputFluidAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RefiningUnitRecipe recipe) {
            InputEntry entry = recipe.getInput();
            entry.ingredient().toNetwork(buf);
            buf.writeInt(entry.count());
            buf.writeItem(recipe.output);

            boolean hasFluid = recipe.requiresFluid();
            buf.writeBoolean(hasFluid);
            if (hasFluid) {
                buf.writeResourceLocation(recipe.requiredFluid);
                buf.writeInt(recipe.requiredFluidAmount);
            }

            boolean hasOutputFluid = recipe.producesFluid();
            buf.writeBoolean(hasOutputFluid);
            if (hasOutputFluid) {
                buf.writeResourceLocation(recipe.outputFluid);
                buf.writeInt(recipe.outputFluidAmount);
            }
        }
    }
}
