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

public class FillingUnitRecipe implements Recipe<Container> {
    public static final int FLUID_PROCESSING_TIME = 40;
    public static final int DEFAULT_PROCESSING_TIME = 200;

    private final ResourceLocation id;
    private final NonNullList<InputEntry> input;
    private final ItemStack output;
    private final ResourceLocation requiredFluid;
    private final int requiredFluidAmount;

    public FillingUnitRecipe(ResourceLocation id, NonNullList<InputEntry> input, ItemStack output) {
        this(id, input, output, null, 0);
    }

    public FillingUnitRecipe(ResourceLocation id, NonNullList<InputEntry> input, ItemStack output,
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

    public int getProcessingTime() {
        return requiresFluid() ? FLUID_PROCESSING_TIME : DEFAULT_PROCESSING_TIME;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide()) return false;

        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inputs.add(inventory.getItem(i));
        }

        for (InputEntry inputEntry : input) {
            boolean matched = false;
            for (ItemStack stack : inputs) {
                if (inputEntry.ingredient().test(stack)) {
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

    public NonNullList<InputEntry> getInput() {
        return input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (InputEntry entry : input) {
            ingredients.add(entry.ingredient());
        }
        return ingredients;
    }

    public static class Type implements RecipeType<FillingUnitRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "filling_unit";
    }

    public static class Serializer implements RecipeSerializer<FillingUnitRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "filling_unit";

        @Override
        public FillingUnitRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "input");
            NonNullList<InputEntry> inputs = NonNullList.withSize(ingredients.size(), InputEntry.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                JsonObject obj = ingredients.get(i).getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(obj);
                int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
                inputs.set(i, new InputEntry(ingredient, count));
            }
            
            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (json.has("fluid")) {
                JsonObject fluidObj = GsonHelper.getAsJsonObject(json, "fluid");
                requiredFluid = new ResourceLocation(GsonHelper.getAsString(fluidObj, "id"));
                requiredFluidAmount = fluidObj.has("amount") ? GsonHelper.getAsInt(fluidObj, "amount") : 1000;
            }

            return new FillingUnitRecipe(id, inputs, output, requiredFluid, requiredFluidAmount);
        }

        @Override
        public FillingUnitRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<InputEntry> inputs = NonNullList.withSize(buf.readInt(), InputEntry.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                Ingredient ingredient = Ingredient.fromNetwork(buf);
                int count = buf.readInt();
                inputs.set(i, new InputEntry(ingredient, count));
            }
            ItemStack output = buf.readItem();

            boolean hasFluid = buf.readBoolean();
            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (hasFluid) {
                requiredFluid = buf.readResourceLocation();
                requiredFluidAmount = buf.readInt();
            }

            return new FillingUnitRecipe(id, inputs, output, requiredFluid, requiredFluidAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FillingUnitRecipe recipe) {
            buf.writeInt(recipe.input.size());
            for (InputEntry entry : recipe.input) {
                entry.ingredient().toNetwork(buf);
                buf.writeInt(entry.count());
            }
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
