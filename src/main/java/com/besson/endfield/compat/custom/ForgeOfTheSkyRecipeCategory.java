package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModFluidHelper;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.ForgeOfTheSkyRecipe;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class ForgeOfTheSkyRecipeCategory extends AbstractMachineRecipeCategory<ForgeOfTheSkyRecipe> {
    public static final RecipeType<ForgeOfTheSkyRecipe> FORGE_OF_THE_SKY =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "forge_of_the_sky"), ForgeOfTheSkyRecipe.class);

    public ForgeOfTheSkyRecipeCategory(IGuiHelper helper) {
        super(FORGE_OF_THE_SKY, "blockEntity.forge_of_the_sky", helper, ModBlocks.FORGE_OF_THE_SKY.get());
    }

    @Override
    protected int inputSlotCount(ForgeOfTheSkyRecipe recipe) {
        return 1 + (recipe.requiresFluid() ? 1 : 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ForgeOfTheSkyRecipe recipe, IFocusGroup focuses) {
        int inputs = slots(inputSlotCount(recipe));
        int startX = inputStartX(inputs, 1);

        builder.addSlot(RecipeIngredientRole.INPUT, startX, SLOT_Y)
                .setStandardSlotBackground()
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput()));

        if (recipe.requiresFluid()) {
            FluidStack fluid = ModFluidHelper.stack(recipe.getRequiredFluid(), recipe.getRequiredFluidAmount());
            if (!fluid.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, startX + PITCH, SLOT_Y)
                        .setStandardSlotBackground()
                        .setFluidRenderer(fluid.getAmount(), false, SLOT_SIZE - 2, SLOT_SIZE - 2)
                        .addIngredient(ForgeTypes.FLUID_STACK, fluid);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(inputs, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
