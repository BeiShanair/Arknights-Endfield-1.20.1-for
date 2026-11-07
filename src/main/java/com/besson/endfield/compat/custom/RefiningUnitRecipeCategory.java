package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModFluidHelper;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.RefiningUnitRecipe;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class RefiningUnitRecipeCategory extends AbstractMachineRecipeCategory<RefiningUnitRecipe> {
    public static final RecipeType<RefiningUnitRecipe> REFINING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "refining_unit"), RefiningUnitRecipe.class);

    public RefiningUnitRecipeCategory(IGuiHelper helper) {
        super(REFINING_UNIT, "blockEntity.refining_unit", helper, ModBlocks.REFINING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(RefiningUnitRecipe recipe) {
        return 1 + (recipe.requiresFluid() ? 1 : 0);
    }

    @Override
    protected int outputSlotCount(RefiningUnitRecipe recipe) {
        return 1 + (recipe.producesFluid() ? 1 : 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RefiningUnitRecipe recipe, IFocusGroup focuses) {
        int inputs = slots(inputSlotCount(recipe));
        int outputs = slots(outputSlotCount(recipe));
        int inputX = inputStartX(inputs, outputs);
        int outputX = outputStartX(inputs, outputs);

        builder.addSlot(RecipeIngredientRole.INPUT, inputX, SLOT_Y)
                .setStandardSlotBackground()
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput()));

        if (recipe.requiresFluid()) {
            FluidStack fluid = ModFluidHelper.stack(recipe.getRequiredFluid(), recipe.getRequiredFluidAmount());
            if (!fluid.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, inputX + PITCH, SLOT_Y)
                        .setStandardSlotBackground()
                        .setFluidRenderer(fluid.getAmount(), false, SLOT_SIZE - 2, SLOT_SIZE - 2)
                        .addIngredient(ForgeTypes.FLUID_STACK, fluid);
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputX, SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));

        if (recipe.producesFluid()) {
            FluidStack fluid = ModFluidHelper.stack(recipe.getOutputFluid(), recipe.getOutputFluidAmount());
            if (!fluid.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, outputX + PITCH, SLOT_Y)
                        .setStandardSlotBackground()
                        .setFluidRenderer(fluid.getAmount(), false, SLOT_SIZE - 2, SLOT_SIZE - 2)
                        .addIngredient(ForgeTypes.FLUID_STACK, fluid);
            }
        }
    }
}
