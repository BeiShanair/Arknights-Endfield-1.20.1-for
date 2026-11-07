package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModFluidHelper;
import com.besson.endfield.recipe.custom.PlantingUnitRecipe;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class PlantingRecipeCategory extends AbstractMachineRecipeCategory<PlantingUnitRecipe> {
    public static final RecipeType<PlantingUnitRecipe> PLANTING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "planting_unit"), PlantingUnitRecipe.class);

    public PlantingRecipeCategory(IGuiHelper helper) {
        super(PLANTING_UNIT, "blockEntity.planting_unit", helper, ModBlocks.PLANTING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(PlantingUnitRecipe recipe) {
        return 1 + (recipe.requiresFluid() ? 1 : 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlantingUnitRecipe recipe, IFocusGroup focuses) {
        int inputs = slots(inputSlotCount(recipe));
        int startX = inputStartX(inputs, 1);

        builder.addSlot(RecipeIngredientRole.INPUT, startX, SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(recipe.getInput());

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
