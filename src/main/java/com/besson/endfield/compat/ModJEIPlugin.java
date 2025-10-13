package com.besson.endfield.compat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.custom.*;
import com.besson.endfield.recipe.custom.*;
import com.besson.endfield.screen.custom.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    public static final ResourceLocation JEI_PLUGIN = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return JEI_PLUGIN;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new RefiningUnitRecipeCategory(guiHelper));
        registration.addRecipeCategories(new CrafterRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FillingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FittingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new GearingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new GrindingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new MouldingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ElectricMiningRigMkIIRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ElectricMiningRigRecipeCategory(guiHelper));
        registration.addRecipeCategories(new PortableOriginiumRigRecipeCategory(guiHelper));
        registration.addRecipeCategories(new PackagingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new PlantingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SeedPickingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ShreddingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        RecipeManager recipeManager = mc.level.getRecipeManager();

        registration.addRecipes(RefiningUnitRecipeCategory.REFINING_UNIT,
                recipeManager.getAllRecipesFor(RefiningUnitRecipe.Type.INSTANCE));
        registration.addRecipes(CrafterRecipeCategory.CRAFTER,
                recipeManager.getAllRecipesFor(CrafterRecipe.Type.INSTANCE));
        registration.addRecipes(FillingRecipeCategory.FILLING_UNIT,
                recipeManager.getAllRecipesFor(FillingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(FittingRecipeCategory.FITTING_UNIT,
                recipeManager.getAllRecipesFor(FittingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(GearingRecipeCategory.GEARING_UNIT,
                recipeManager.getAllRecipesFor(GearingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(GrindingRecipeCategory.GRINDING_UNIT,
                recipeManager.getAllRecipesFor(GrindingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(MouldingRecipeCategory.MOULDING_UNIT,
                recipeManager.getAllRecipesFor(MouldingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(PortableOriginiumRigRecipeCategory.PORTABLE_ORIGINIUM_RIG,
                recipeManager.getAllRecipesFor(OreRigRecipe.Type.INSTANCE));
        registration.addRecipes(ElectricMiningRigRecipeCategory.ELECTRIC_MINING_RIG,
                recipeManager.getAllRecipesFor(OreRigRecipe.Type.INSTANCE));
        registration.addRecipes(ElectricMiningRigMkIIRecipeCategory.ELECTRIC_MINING_RIG_MK_II,
                recipeManager.getAllRecipesFor(OreRigRecipe.Type.INSTANCE));
        registration.addRecipes(PackagingRecipeCategory.PACKAGING,
                recipeManager.getAllRecipesFor(PackagingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(PlantingRecipeCategory.PLANTING_UNIT,
                recipeManager.getAllRecipesFor(PlantingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(SeedPickingRecipeCategory.SEED_PICKING_UNIT,
                recipeManager.getAllRecipesFor(SeedPickingUnitRecipe.Type.INSTANCE));
        registration.addRecipes(ShreddingRecipeCategory.SHREDDING_UNIT,
                recipeManager.getAllRecipesFor(ShreddingUnitRecipe.Type.INSTANCE));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.REFINING_UNIT.get()),
                RefiningUnitRecipeCategory.REFINING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRAFTER.get()),
                CrafterRecipeCategory.CRAFTER);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FILLING_UNIT.get()),
                FillingRecipeCategory.FILLING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FITTING_UNIT.get()),
                FittingRecipeCategory.FITTING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GEARING_UNIT.get()),
                GearingRecipeCategory.GEARING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GRINDING_UNIT.get()),
                GrindingRecipeCategory.GRINDING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MOULDING_UNIT.get()),
                MouldingRecipeCategory.MOULDING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PORTABLE_ORIGINIUM_RIG.get()),
                PortableOriginiumRigRecipeCategory.PORTABLE_ORIGINIUM_RIG);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTRIC_MINING_RIG.get()),
                ElectricMiningRigRecipeCategory.ELECTRIC_MINING_RIG);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTRIC_MINING_RIG_MK_II.get()),
                ElectricMiningRigMkIIRecipeCategory.ELECTRIC_MINING_RIG_MK_II);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PACKAGING_UNIT.get()),
                PackagingRecipeCategory.PACKAGING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PLANTING_UNIT.get()),
                PlantingRecipeCategory.PLANTING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SEED_PICKING_UNIT.get()),
                SeedPickingRecipeCategory.SEED_PICKING_UNIT);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SHREDDING_UNIT.get()),
                ShreddingRecipeCategory.SHREDDING_UNIT);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(RefiningUnitScreen.class, 60, 30, 20, 30,
                RefiningUnitRecipeCategory.REFINING_UNIT);
        registration.addRecipeClickArea(CrafterScreen.class, 90, 36, 20, 30,
                CrafterRecipeCategory.CRAFTER);
        registration.addRecipeClickArea(FillingUnitScreen.class, 90, 36, 20, 30,
                FillingRecipeCategory.FILLING_UNIT);
        registration.addRecipeClickArea(FittingUnitScreen.class, 60, 30, 20, 30,
                FittingRecipeCategory.FITTING_UNIT);
        registration.addRecipeClickArea(GearingUnitScreen.class, 90, 36, 20, 30,
                GearingRecipeCategory.GEARING_UNIT);
        registration.addRecipeClickArea(GrindingUnitScreen.class, 90, 36, 20, 30,
                GrindingRecipeCategory.GRINDING_UNIT);
        registration.addRecipeClickArea(MouldingUnitScreen.class, 60, 30, 20, 30,
                MouldingRecipeCategory.MOULDING_UNIT);
        registration.addRecipeClickArea(PortableOriginiumRigScreen.class, 60, 30, 20, 30,
                PortableOriginiumRigRecipeCategory.PORTABLE_ORIGINIUM_RIG);
        registration.addRecipeClickArea(ElectricMiningRigScreen.class, 60, 30, 20, 30,
                ElectricMiningRigRecipeCategory.ELECTRIC_MINING_RIG);
        registration.addRecipeClickArea(ElectricMiningRigMkIIScreen.class, 60, 30, 20, 30,
                ElectricMiningRigMkIIRecipeCategory.ELECTRIC_MINING_RIG_MK_II);
        registration.addRecipeClickArea(PackagingUnitScreen.class, 90, 36, 20, 30,
                PackagingRecipeCategory.PACKAGING);
        registration.addRecipeClickArea(PlantingUnitScreen.class, 60, 30, 20, 30,
                PlantingRecipeCategory.PLANTING_UNIT);
        registration.addRecipeClickArea(SeedPickingUnitScreen.class, 60, 30, 20, 30,
                SeedPickingRecipeCategory.SEED_PICKING_UNIT);
        registration.addRecipeClickArea(ShreddingUnitScreen.class, 60, 30, 20, 30,
                ShreddingRecipeCategory.SHREDDING_UNIT);
    }
}
