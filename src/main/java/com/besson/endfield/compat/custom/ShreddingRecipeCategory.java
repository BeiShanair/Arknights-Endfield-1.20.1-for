package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.ShreddingUnitRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ShreddingRecipeCategory implements IRecipeCategory<ShreddingUnitRecipe> {
    public static final RecipeType<ShreddingUnitRecipe> SHREDDING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "shredding_unit"), ShreddingUnitRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/shredding_unit.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ShreddingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SHREDDING_UNIT.get()));
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public RecipeType<ShreddingUnitRecipe> getRecipeType() {
        return SHREDDING_UNIT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.shredding_unit");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ShreddingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 7)
                .addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 55)
                .addItemStack(recipe.getResultItem(null));
    }
}
