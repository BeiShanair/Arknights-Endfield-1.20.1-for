package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.GearingUnitRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GearingRecipeCategory extends AbstractMachineRecipeCategory<GearingUnitRecipe> {
    public static final RecipeType<GearingUnitRecipe> GEARING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "gearing_unit"), GearingUnitRecipe.class);

    public GearingRecipeCategory(IGuiHelper helper) {
        super(GEARING_UNIT, "blockEntity.gearing_unit", helper, ModBlocks.GEARING_UNIT.get());
    }

    @Override
    protected int inputSlotCount(GearingUnitRecipe recipe) {
        return recipe.getInput().size();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GearingUnitRecipe recipe, IFocusGroup focuses) {
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
