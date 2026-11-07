package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.MouldingUnitRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class MouldingRecipeCategory extends AbstractMachineRecipeCategory<MouldingUnitRecipe> {
    public static final RecipeType<MouldingUnitRecipe> MOULDING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "moulding_unit"), MouldingUnitRecipe.class);

    public MouldingRecipeCategory(IGuiHelper helper) {
        super(MOULDING_UNIT, "blockEntity.moulding_unit", helper, ModBlocks.MOULDING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(MouldingUnitRecipe recipe) {
        return 1;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MouldingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, inputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
