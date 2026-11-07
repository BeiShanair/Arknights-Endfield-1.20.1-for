package com.besson.endfield.recipe.custom;

import com.besson.endfield.recipe.InputEntry;
import com.besson.endfield.recipe.ReactorResource;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReactorCrucibleRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<InputEntry> itemInputs;
    private final NonNullList<FluidInputEntry> fluidInputs;
    private final NonNullList<ItemStack> itemOutputs;
    private final NonNullList<FluidOutputEntry> fluidOutputs;

    public ReactorCrucibleRecipe(ResourceLocation id,
                                 NonNullList<InputEntry> itemInputs,
                                 NonNullList<FluidInputEntry> fluidInputs,
                                 NonNullList<ItemStack> itemOutputs,
                                 NonNullList<FluidOutputEntry> fluidOutputs) {
        this.id = id;
        this.itemInputs = itemInputs;
        this.fluidInputs = fluidInputs;
        this.itemOutputs = itemOutputs;
        this.fluidOutputs = fluidOutputs;
    }

    public boolean canCraft(Map<ReactorResource, Long> resources) {
        for (InputEntry input : itemInputs) {
            long available = 0;
            for (Map.Entry<ReactorResource, Long> entry : resources.entrySet()) {
                ReactorResource resource = entry.getKey();
                if (resource.isItem() && input.ingredient().test(resource.itemStack())) {
                    available += entry.getValue();
                }
            }
            if (available < input.count()) return false;
        }

        for (FluidInputEntry input : fluidInputs) {
            ReactorResource resource = toResource(input);
            if (resource.isBlank()) return false;
            if (resources.getOrDefault(resource, 0L) < input.amount()) return false;
        }
        return true;
    }

    public boolean canFitOutputs(Map<ReactorResource, Long> resources, int maxResourceTypes) {
        Map<ReactorResource, Long> simulated = new LinkedHashMap<>(resources);
        for (ItemStack output : itemOutputs) {
            if (!canAdd(simulated, ReactorResource.item(output), output.getCount(), maxResourceTypes)) return false;
        }
        for (FluidOutputEntry output : fluidOutputs) {
            if (!canAdd(simulated, toResource(output), output.amount(), maxResourceTypes)) return false;
        }
        return true;
    }

    public void consumeInputs(Map<ReactorResource, Long> resources) {
        for (InputEntry input : itemInputs) {
            int remaining = input.count();
            Iterator<Map.Entry<ReactorResource, Long>> iterator = resources.entrySet().iterator();
            while (iterator.hasNext() && remaining > 0) {
                Map.Entry<ReactorResource, Long> entry = iterator.next();
                ReactorResource resource = entry.getKey();
                if (!resource.isItem() || !input.ingredient().test(resource.itemStack())) continue;
                long consumed = Math.min(entry.getValue(), remaining);
                entry.setValue(entry.getValue() - consumed);
                remaining -= (int) consumed;
                if (entry.getValue() <= 0) iterator.remove();
            }
        }

        for (FluidInputEntry input : fluidInputs) {
            ReactorResource resource = toResource(input);
            long stored = resources.getOrDefault(resource, 0L);
            long remaining = stored - input.amount();
            if (remaining > 0) {
                resources.put(resource, remaining);
            } else {
                resources.remove(resource);
            }
        }
    }

    public void addOutputs(Map<ReactorResource, Long> resources) {
        for (ItemStack output : itemOutputs) {
            resources.merge(ReactorResource.item(output), (long) output.getCount(), Long::sum);
        }
        for (FluidOutputEntry output : fluidOutputs) {
            ReactorResource resource = toResource(output);
            if (resource.isBlank()) continue;
            resources.merge(resource, (long) output.amount(), Long::sum);
        }
    }

    private static boolean canAdd(Map<ReactorResource, Long> resources, ReactorResource resource, long amount, int maxResourceTypes) {
        if (resource.isBlank() || amount <= 0) return false;
        long stored = resources.getOrDefault(resource, 0L);
        if (stored <= 0 && resources.size() >= maxResourceTypes) return false;
        long nextAmount = stored + amount;
        if (nextAmount > resource.getMaxAmount()) return false;
        resources.put(resource, nextAmount);
        return true;
    }

    private static ReactorResource toResource(FluidInputEntry entry) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(entry.fluid());
        if (fluid == null) return ReactorResource.EMPTY;
        return ReactorResource.fluid(new FluidStack(fluid, 1));
    }

    private static ReactorResource toResource(FluidOutputEntry entry) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(entry.fluid());
        if (fluid == null) return ReactorResource.EMPTY;
        return ReactorResource.fluid(new FluidStack(fluid, 1));
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        return getResultItem(registryManager).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryManager) {
        return itemOutputs.isEmpty() ? ItemStack.EMPTY : itemOutputs.get(0);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (InputEntry entry : itemInputs) {
            ingredients.add(entry.ingredient());
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

    @Override
    public boolean isSpecial() {
        return true;
    }

    public NonNullList<InputEntry> getItemInputs() {
        return itemInputs;
    }

    public NonNullList<FluidInputEntry> getFluidInputs() {
        return fluidInputs;
    }

    public NonNullList<ItemStack> getItemOutputs() {
        return itemOutputs;
    }

    public NonNullList<FluidOutputEntry> getFluidOutputs() {
        return fluidOutputs;
    }

    public record FluidInputEntry(ResourceLocation fluid, int amount) {
    }

    public record FluidOutputEntry(ResourceLocation fluid, int amount) {
    }

    public static class Type implements RecipeType<ReactorCrucibleRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "reactor_crucible";
    }

    public static class Serializer implements RecipeSerializer<ReactorCrucibleRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "reactor_crucible";

        @Override
        public ReactorCrucibleRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray input = GsonHelper.getAsJsonArray(json, "input");
            NonNullList<InputEntry> itemInputs = NonNullList.create();
            NonNullList<FluidInputEntry> fluidInputs = NonNullList.create();
            for (int i = 0; i < input.size(); i++) {
                JsonObject object = input.get(i).getAsJsonObject();
                if (object.has("fluid")) {
                    fluidInputs.add(new FluidInputEntry(readFluidId(object),
                            GsonHelper.getAsInt(object, "amount", 1000)));
                } else {
                    Ingredient ingredient = Ingredient.fromJson(object);
                    itemInputs.add(new InputEntry(ingredient, GsonHelper.getAsInt(object, "count", 1)));
                }
            }

            JsonArray output = GsonHelper.getAsJsonArray(json, "output");
            NonNullList<ItemStack> itemOutputs = NonNullList.create();
            NonNullList<FluidOutputEntry> fluidOutputs = NonNullList.create();
            for (int i = 0; i < output.size(); i++) {
                JsonObject object = output.get(i).getAsJsonObject();
                if (object.has("fluid")) {
                    fluidOutputs.add(new FluidOutputEntry(readFluidId(object),
                            GsonHelper.getAsInt(object, "amount", 1000)));
                } else {
                    itemOutputs.add(ShapedRecipe.itemStackFromJson(object));
                }
            }

            return new ReactorCrucibleRecipe(id, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        }

        @Override
        public ReactorCrucibleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<InputEntry> itemInputs = NonNullList.create();
            int itemInputSize = buf.readInt();
            for (int i = 0; i < itemInputSize; i++) {
                itemInputs.add(new InputEntry(Ingredient.fromNetwork(buf), buf.readInt()));
            }

            NonNullList<FluidInputEntry> fluidInputs = NonNullList.create();
            int fluidInputSize = buf.readInt();
            for (int i = 0; i < fluidInputSize; i++) {
                fluidInputs.add(new FluidInputEntry(buf.readResourceLocation(), buf.readInt()));
            }

            NonNullList<ItemStack> itemOutputs = NonNullList.create();
            int itemOutputSize = buf.readInt();
            for (int i = 0; i < itemOutputSize; i++) {
                itemOutputs.add(buf.readItem());
            }

            NonNullList<FluidOutputEntry> fluidOutputs = NonNullList.create();
            int fluidOutputSize = buf.readInt();
            for (int i = 0; i < fluidOutputSize; i++) {
                fluidOutputs.add(new FluidOutputEntry(buf.readResourceLocation(), buf.readInt()));
            }

            return new ReactorCrucibleRecipe(id, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ReactorCrucibleRecipe recipe) {
            buf.writeInt(recipe.itemInputs.size());
            for (InputEntry input : recipe.itemInputs) {
                input.ingredient().toNetwork(buf);
                buf.writeInt(input.count());
            }

            buf.writeInt(recipe.fluidInputs.size());
            for (FluidInputEntry input : recipe.fluidInputs) {
                buf.writeResourceLocation(input.fluid());
                buf.writeInt(input.amount());
            }

            buf.writeInt(recipe.itemOutputs.size());
            for (ItemStack output : recipe.itemOutputs) {
                buf.writeItem(output);
            }

            buf.writeInt(recipe.fluidOutputs.size());
            for (FluidOutputEntry output : recipe.fluidOutputs) {
                buf.writeResourceLocation(output.fluid());
                buf.writeInt(output.amount());
            }
        }

        private static ResourceLocation readFluidId(JsonObject object) {
            return new ResourceLocation(GsonHelper.getAsString(object, "fluid"));
        }
    }
}
