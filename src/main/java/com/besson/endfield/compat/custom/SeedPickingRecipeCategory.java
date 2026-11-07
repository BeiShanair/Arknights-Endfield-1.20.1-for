package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.SeedPickingUnitRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class SeedPickingRecipeCategory extends AbstractMachineRecipeCategory<SeedPickingUnitRecipe> {
    public static final RecipeType<SeedPickingUnitRecipe> SEED_PICKING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "seed_picking_unit"), SeedPickingUnitRecipe.class);

    public SeedPickingRecipeCategory(IGuiHelper helper) {
        super(SEED_PICKING_UNIT, "blockEntity.seed_picking_unit", helper, ModBlocks.SEED_PICKING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(SeedPickingUnitRecipe recipe) {
        return 1;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SeedPickingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, inputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
