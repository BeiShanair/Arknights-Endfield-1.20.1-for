package com.besson.endfield.recipe.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CrafterRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Map<ItemLike, Integer> required;
    private final ItemStack output;

    public CrafterRecipe(ResourceLocation id, Map<ItemLike, Integer> required, ItemStack output) {
        this.id = id;
        this.required = required;
        this.output = output;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        for (Map.Entry<ItemLike, Integer> entry : required.entrySet()) {
            int count = 0;
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (stack.getItem().equals(entry.getKey().asItem())) {
                    count += stack.getCount();
                }
            }
            if (count < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        for (Map.Entry<ItemLike, Integer> entry : required.entrySet()) {
            int need = entry.getValue();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (stack.getItem().equals(entry.getKey().asItem())) {
                    int removed = Math.min(stack.getCount(), need);
                    stack.shrink(removed);
                }
            }
        }
        return output.copy();
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

    public Map<ItemLike, Integer> getRequired() {
        return required;
    }

    public static class Type implements RecipeType<CrafterRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "crafter";
    }

    public static class Serializer implements RecipeSerializer<CrafterRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "crafter";

        @Override
        public CrafterRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject input = GsonHelper.getAsJsonObject(json, "input");
            Map<ItemLike, Integer> required = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry: input.entrySet()) {
                ItemLike item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(entry.getKey()));
                int count = entry.getValue().getAsInt();
                required.put(item, count);
            }

            JsonObject output = GsonHelper.getAsJsonObject(json, "output");
            ItemLike result = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(GsonHelper.getAsString(output, "item")));
            int count = GsonHelper.getAsInt(output, "count", 1);

            return new CrafterRecipe(id, required, new ItemStack(result.asItem(), count));
        }

        @Override
        public CrafterRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int size = buf.readInt();
            Map<ItemLike, Integer> required = new HashMap<>();
            for (int i = 0; i < size; i++) {
                ItemLike item = buf.readItem().getItem();
                int count = buf.readInt();
                required.put(item, count);
            }
            ItemStack output = buf.readItem();
            return new CrafterRecipe(id, required, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CrafterRecipe recipe) {
            buf.writeInt(recipe.getRequired().size());
            for (Map.Entry<ItemLike, Integer> entry : recipe.getRequired().entrySet()) {
                buf.writeItem(new ItemStack(entry.getKey().asItem()));
                buf.writeInt(entry.getValue());
            }
            buf.writeItem(recipe.output);
        }
    }
}
