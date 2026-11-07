package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.ShreddingUnitRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class ShreddingRecipeCategory extends AbstractMachineRecipeCategory<ShreddingUnitRecipe> {
    public static final RecipeType<ShreddingUnitRecipe> SHREDDING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "shredding_unit"), ShreddingUnitRecipe.class);

    public ShreddingRecipeCategory(IGuiHelper helper) {
        super(SHREDDING_UNIT, "blockEntity.shredding_unit", helper, ModBlocks.SHREDDING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(ShreddingUnitRecipe recipe) {
        return 1;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ShreddingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, inputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
