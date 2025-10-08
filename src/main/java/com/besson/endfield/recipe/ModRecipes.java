package com.besson.endfield.recipe;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.recipe.custom.CrafterRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CrafterRecipe>> CRAFTER_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafter", () -> CrafterRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
