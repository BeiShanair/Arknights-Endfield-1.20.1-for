package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.FillingUnitRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FillingRecipeCategory implements IRecipeCategory<FillingUnitRecipe> {
    public static final RecipeType<FillingUnitRecipe> FILLING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "filling_recipe"), FillingUnitRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/filling_unit.png");

    private final IDrawable background;
    private final IDrawable icon;

    public FillingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FILLING_UNIT.get()));
    }
    @Override
    public RecipeType<FillingUnitRecipe> getRecipeType() {
        return FILLING_UNIT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.filling_unit");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FillingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 18)
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput().get(0)));
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 45)
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput().get(1)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 31)
                .addItemStack(recipe.getResultItem(null));
    }
}
