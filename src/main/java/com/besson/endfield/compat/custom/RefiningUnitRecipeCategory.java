package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.RefiningUnitRecipe;
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

public class RefiningUnitRecipeCategory implements IRecipeCategory<RefiningUnitRecipe> {
    public static final RecipeType<RefiningUnitRecipe> REFINING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "refining_unit"), RefiningUnitRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/refining_unit.png");

    private final IDrawable background;
    private final IDrawable icon;

    public RefiningUnitRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.REFINING_UNIT.get()));
    }

    @Override
    public RecipeType<RefiningUnitRecipe> getRecipeType() {
        return REFINING_UNIT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.refining_unit");
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, RefiningUnitRecipe recipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 76, 7)
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput()));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 76, 55)
                .addItemStack(recipe.getResultItem(null));
    }
}
