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

public class SeparatingUnitRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final InputEntry input;
    private final ItemStack output;
    private final ResourceLocation outputFluid;
    private final int outputFluidAmount;

    public SeparatingUnitRecipe(ResourceLocation id, InputEntry input, ItemStack output) {
        this(id, input, output, null, 0);
    }

    public SeparatingUnitRecipe(ResourceLocation id, InputEntry input, ItemStack output,
                                ResourceLocation outputFluid, int outputFluidAmount) {
        this.id = id;
        this.input = input;
        this.output = output;
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

    public static class Type implements RecipeType<SeparatingUnitRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "separating_unit";
    }

    public static class Serializer implements RecipeSerializer<SeparatingUnitRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "separating_unit";

        @Override
        public SeparatingUnitRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject ingredients = GsonHelper.getAsJsonObject(json, "input");
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            Ingredient ingredient = Ingredient.fromJson(ingredients);
            int count = ingredients.has("count") ? GsonHelper.getAsInt(ingredients, "count") : 1;
            InputEntry inputs = new InputEntry(ingredient, count);

            ResourceLocation outputFluid = null;
            int outputFluidAmount = 0;
            if (json.has("output_fluid")) {
                JsonObject fluidObj = GsonHelper.getAsJsonObject(json, "output_fluid");
                outputFluid = new ResourceLocation(GsonHelper.getAsString(fluidObj, "id"));
                outputFluidAmount = fluidObj.has("amount") ? GsonHelper.getAsInt(fluidObj, "amount") : 1000;
            }

            return new SeparatingUnitRecipe(id, inputs, output, outputFluid, outputFluidAmount);
        }

        @Override
        public SeparatingUnitRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int count = buf.readInt();
            InputEntry inputs = new InputEntry(ingredient, count);
            ItemStack output = buf.readItem();

            boolean hasOutputFluid = buf.readBoolean();
            ResourceLocation outputFluid = null;
            int outputFluidAmount = 0;
            if (hasOutputFluid) {
                outputFluid = buf.readResourceLocation();
                outputFluidAmount = buf.readInt();
            }

            return new SeparatingUnitRecipe(id, inputs, output, outputFluid, outputFluidAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SeparatingUnitRecipe recipe) {
            InputEntry entry = recipe.getInput();
            entry.ingredient().toNetwork(buf);
            buf.writeInt(entry.count());
            buf.writeItem(recipe.output);

            boolean hasOutputFluid = recipe.producesFluid();
            buf.writeBoolean(hasOutputFluid);
            if (hasOutputFluid) {
                buf.writeResourceLocation(recipe.outputFluid);
                buf.writeInt(recipe.outputFluidAmount);
            }
        }
    }
}
