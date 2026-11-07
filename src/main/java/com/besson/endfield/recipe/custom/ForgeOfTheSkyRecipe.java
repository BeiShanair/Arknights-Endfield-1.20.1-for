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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ForgeOfTheSkyRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final InputEntry input;
    private final ItemStack output;
    private final ResourceLocation requiredFluid;
    private final int requiredFluidAmount;
    @Nullable
    private final Integer craftTime;

    public ForgeOfTheSkyRecipe(ResourceLocation id, InputEntry input, ItemStack output,
                               ResourceLocation requiredFluid, int requiredFluidAmount,
                               @Nullable Integer craftTime) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.requiredFluid = requiredFluid;
        this.requiredFluidAmount = requiredFluidAmount;
        this.craftTime = craftTime;
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
    
    public Optional<Integer> getCraftTime() {
        return Optional.ofNullable(craftTime);
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

    public static class Type implements RecipeType<ForgeOfTheSkyRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "forge_of_the_sky";
    }

    public static class Serializer implements RecipeSerializer<ForgeOfTheSkyRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "forge_of_the_sky";

        @Override
        public ForgeOfTheSkyRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject ingredientObj = GsonHelper.getAsJsonObject(json, "input");
            Ingredient ingredient = Ingredient.fromJson(ingredientObj);
            int count = ingredientObj.has("count") ? GsonHelper.getAsInt(ingredientObj, "count") : 1;
            InputEntry input = new InputEntry(ingredient, count);

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (json.has("fluid")) {
                JsonObject fluidObj = GsonHelper.getAsJsonObject(json, "fluid");
                requiredFluid = new ResourceLocation(GsonHelper.getAsString(fluidObj, "id"));
                requiredFluidAmount = fluidObj.has("amount") ? GsonHelper.getAsInt(fluidObj, "amount") : 1000;
            }

            Integer craftTime = json.has("craft_time") ? GsonHelper.getAsInt(json, "craft_time") : null;

            return new ForgeOfTheSkyRecipe(id, input, output, requiredFluid, requiredFluidAmount, craftTime);
        }

        @Override
        public ForgeOfTheSkyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int count = buf.readInt();
            InputEntry input = new InputEntry(ingredient, count);
            ItemStack output = buf.readItem();

            boolean hasFluid = buf.readBoolean();
            ResourceLocation requiredFluid = null;
            int requiredFluidAmount = 0;
            if (hasFluid) {
                requiredFluid = buf.readResourceLocation();
                requiredFluidAmount = buf.readInt();
            }

            boolean hasCraftTime = buf.readBoolean();
            Integer craftTime = hasCraftTime ? buf.readInt() : null;

            return new ForgeOfTheSkyRecipe(id, input, output, requiredFluid, requiredFluidAmount, craftTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ForgeOfTheSkyRecipe recipe) {
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

            buf.writeBoolean(recipe.craftTime != null);
            if (recipe.craftTime != null) {
                buf.writeInt(recipe.craftTime);
            }
        }
    }
}
