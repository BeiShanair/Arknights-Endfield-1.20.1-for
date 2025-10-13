package com.besson.endfield.recipe;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.recipe.custom.*;
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
    public static final RegistryObject<RecipeSerializer<OreRigRecipe>> ORE_RIG_SERIALIZER =
            RECIPE_SERIALIZERS.register("ore_rig", () -> OreRigRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<RefiningUnitRecipe>> REFINING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("refining_unit", () -> RefiningUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<FillingUnitRecipe>> FILLING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("filling_unit", () -> FillingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<FittingUnitRecipe>> FITTING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("fitting_unit", () -> FittingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<GearingUnitRecipe>> GEARING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("gearing_unit", () -> GearingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<GrindingUnitRecipe>> GRINDING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("grinding_unit", () -> GrindingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<MouldingUnitRecipe>> MOULDING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("moulding_unit", () -> MouldingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<PackagingUnitRecipe>> PACKAGING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("packaging_unit", () -> PackagingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<PlantingUnitRecipe>> PLANTING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("planting_unit", () -> PlantingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<SeedPickingUnitRecipe>> SEED_PICKING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("seed_picking_unit", () -> SeedPickingUnitRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ShreddingUnitRecipe>> SHREDDING_UNIT_SERIALIZER =
            RECIPE_SERIALIZERS.register("shredding_unit", () -> ShreddingUnitRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
