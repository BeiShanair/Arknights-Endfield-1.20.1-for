package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModFluidHelper;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.InputEntry;
import com.besson.endfield.recipe.custom.FillingUnitRecipe;
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

public class FillingRecipeCategory extends AbstractMachineRecipeCategory<FillingUnitRecipe> {
    public static final RecipeType<FillingUnitRecipe> FILLING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "filling_recipe"), FillingUnitRecipe.class);

    public FillingRecipeCategory(IGuiHelper helper) {
        super(FILLING_UNIT, "blockEntity.filling_unit", helper, ModBlocks.FILLING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(FillingUnitRecipe recipe) {
        return recipe.getInput().size() + (recipe.requiresFluid() ? 1 : 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FillingUnitRecipe recipe, IFocusGroup focuses) {
        int inputs = slots(inputSlotCount(recipe));
        int startX = inputStartX(inputs, 1);

        int index = 0;
        for (InputEntry entry : recipe.getInput()) {
            if (index >= inputs) break;
            List<ItemStack> stacks = ModItemHelper.fromInputEntries(entry);
            if (stacks.isEmpty()) continue;
            builder.addSlot(RecipeIngredientRole.INPUT, startX + index * PITCH, SLOT_Y)
                    .setStandardSlotBackground()
                    .addItemStacks(stacks);
            index++;
        }

        if (recipe.requiresFluid() && index < inputs) {
            FluidStack fluid = ModFluidHelper.stack(recipe.getRequiredFluid(), recipe.getRequiredFluidAmount());
            if (!fluid.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, startX + index * PITCH, SLOT_Y)
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
