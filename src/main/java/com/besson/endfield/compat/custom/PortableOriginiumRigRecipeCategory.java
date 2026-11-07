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

public class PortableOriginiumRigRecipeCategory extends AbstractMachineRecipeCategory<OreRigRecipe> {
    public static final RecipeType<OreRigRecipe> PORTABLE_ORIGINIUM_RIG =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "portable_originium_rig"), OreRigRecipe.class);

    public PortableOriginiumRigRecipeCategory(IGuiHelper helper) {
        super(PORTABLE_ORIGINIUM_RIG, "blockEntity.portable_originium_rig", helper, ModBlocks.PORTABLE_ORIGINIUM_RIG.get());
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
