package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.CrafterRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Map;

public class CrafterRecipeCategory extends AbstractMachineRecipeCategory<CrafterRecipe> {
    public static final RecipeType<CrafterRecipe> CRAFTER =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "crafter"), CrafterRecipe.class);

    private static final int MAX_INPUTS = 5;

    public CrafterRecipeCategory(IGuiHelper helper) {
        super(CRAFTER, "blockEntity.crafter", helper, ModBlocks.CRAFTER.get());
    }

    @Override
    protected int maxSlots() {
        return MAX_INPUTS;
    }

    @Override
    protected int inputSlotCount(CrafterRecipe recipe) {
        return recipe.getRequired().size();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrafterRecipe recipe, IFocusGroup focuses) {
        int inputs = slots(inputSlotCount(recipe));
        int startX = inputStartX(inputs, 1);

        int index = 0;
        for (Map.Entry<ItemLike, Integer> entry : recipe.getRequired().entrySet()) {
            if (index >= inputs) break;
            builder.addSlot(RecipeIngredientRole.INPUT, startX + index * PITCH, SLOT_Y)
                    .setStandardSlotBackground()
                    .addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(entry.getKey().asItem(), entry.getValue()));
            index++;
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(inputs, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
