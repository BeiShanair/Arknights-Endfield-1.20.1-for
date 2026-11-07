package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.FittingUnitRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class FittingRecipeCategory extends AbstractMachineRecipeCategory<FittingUnitRecipe> {
    public static final RecipeType<FittingUnitRecipe> FITTING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "fitting_unit"), FittingUnitRecipe.class);

    public FittingRecipeCategory(IGuiHelper helper) {
        super(FITTING_UNIT, "blockEntity.fitting_unit", helper, ModBlocks.FITTING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(FittingUnitRecipe recipe) {
        return 1;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FittingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, inputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
