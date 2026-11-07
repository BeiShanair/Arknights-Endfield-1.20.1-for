package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModFluidHelper;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.InputEntry;
import com.besson.endfield.recipe.custom.ReactorCrucibleRecipe;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ReactorCrucibleRecipeCategory extends AbstractMachineRecipeCategory<ReactorCrucibleRecipe> {
    public static final RecipeType<ReactorCrucibleRecipe> REACTOR_CRUCIBLE =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "reactor_crucible"), ReactorCrucibleRecipe.class);

    public ReactorCrucibleRecipeCategory(IGuiHelper helper) {
        super(REACTOR_CRUCIBLE, "blockEntity.reactor_crucible", helper, ModBlocks.REACTOR_CRUCIBLE.get());
    }

    @Override
    protected int inputSlotCount(ReactorCrucibleRecipe recipe) {
        return recipe.getItemInputs().size() + recipe.getFluidInputs().size();
    }

    @Override
    protected int outputSlotCount(ReactorCrucibleRecipe recipe) {
        return recipe.getItemOutputs().size() + recipe.getFluidOutputs().size();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ReactorCrucibleRecipe recipe, IFocusGroup focuses) {
        int inputs = slots(inputSlotCount(recipe));
        int outputs = slots(outputSlotCount(recipe));
        int inputX = inputStartX(inputs, outputs);
        int outputX = outputStartX(inputs, outputs);

        int index = 0;
        
        for (InputEntry entry : recipe.getItemInputs()) {
            if (index >= inputs) break;
            List<ItemStack> stacks = ModItemHelper.fromInputEntries(entry);
            if (stacks.isEmpty()) continue;
            builder.addSlot(RecipeIngredientRole.INPUT, inputX + index * PITCH, SLOT_Y)
                    .setStandardSlotBackground()
                    .addItemStacks(stacks);
            index++;
        }
        
        for (ReactorCrucibleRecipe.FluidInputEntry entry : recipe.getFluidInputs()) {
            if (index >= inputs) break;
            FluidStack stack = ModFluidHelper.stack(entry.fluid(), entry.amount());
            if (stack.isEmpty()) continue;
            builder.addSlot(RecipeIngredientRole.INPUT, inputX + index * PITCH, SLOT_Y)
                    .setStandardSlotBackground()
                    .setFluidRenderer(stack.getAmount(), false, SLOT_SIZE - 2, SLOT_SIZE - 2)
                    .addIngredient(ForgeTypes.FLUID_STACK, stack);
            index++;
        }

        int outputIndex = 0;
        
        for (ItemStack stack : recipe.getItemOutputs()) {
            if (outputIndex >= outputs) break;
            if (stack.isEmpty()) continue;
            builder.addSlot(RecipeIngredientRole.OUTPUT, outputX + outputIndex * PITCH, SLOT_Y)
                    .setStandardSlotBackground()
                    .addItemStack(stack);
            outputIndex++;
        }
        
        for (ReactorCrucibleRecipe.FluidOutputEntry entry : recipe.getFluidOutputs()) {
            if (outputIndex >= outputs) break;
            FluidStack stack = ModFluidHelper.stack(entry.fluid(), entry.amount());
            if (stack.isEmpty()) continue;
            builder.addSlot(RecipeIngredientRole.OUTPUT, outputX + outputIndex * PITCH, SLOT_Y)
                    .setStandardSlotBackground()
                    .setFluidRenderer(stack.getAmount(), false, SLOT_SIZE - 2, SLOT_SIZE - 2)
                    .addIngredient(ForgeTypes.FLUID_STACK, stack);
            outputIndex++;
        }
    }
}
