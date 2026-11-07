package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.OreRigRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class ElectricMiningRigMkIIRecipeCategory extends AbstractMachineRecipeCategory<OreRigRecipe> {
    public static final RecipeType<OreRigRecipe> ELECTRIC_MINING_RIG_MK_II =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "electric_mining_rig_mk_ii"), OreRigRecipe.class);

    public ElectricMiningRigMkIIRecipeCategory(IGuiHelper helper) {
        super(ELECTRIC_MINING_RIG_MK_II, "blockEntity.electric_mining_rig_mk_ii", helper, ModBlocks.ELECTRIC_MINING_RIG_MK_II.get());
    }

    @Override
    protected int inputSlotCount(OreRigRecipe recipe) {
        return 1;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OreRigRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, inputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(1, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
