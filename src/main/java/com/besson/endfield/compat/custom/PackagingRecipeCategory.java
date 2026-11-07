package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.PackagingUnitRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PackagingRecipeCategory extends AbstractMachineRecipeCategory<PackagingUnitRecipe> {
    public static final RecipeType<PackagingUnitRecipe> PACKAGING =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "packaging"), PackagingUnitRecipe.class);

    public PackagingRecipeCategory(IGuiHelper helper) {
        super(PACKAGING, "blockEntity.packaging_unit", helper, ModBlocks.PACKAGING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(PackagingUnitRecipe recipe) {
        return recipe.getInput().size();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PackagingUnitRecipe recipe, IFocusGroup focuses) {
        int inputs = slots(inputSlotCount(recipe));
        int startX = inputStartX(inputs, 1);

        for (int i = 0; i < inputs; i++) {
            List<ItemStack> stacks = ModItemHelper.fromInputEntries(recipe.getInput().get(i));
            if (stacks.isEmpty()) continue;
            builder.addSlot(RecipeIngredientRole.INPUT, startX + i * PITCH, SLOT_Y)
                    .setStandardSlotBackground()
                    .addItemStacks(stacks);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(inputs, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
