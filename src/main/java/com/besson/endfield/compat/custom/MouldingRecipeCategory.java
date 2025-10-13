package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.MouldingUnitRecipe;
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

public class MouldingRecipeCategory implements IRecipeCategory<MouldingUnitRecipe> {
    public static final RecipeType<MouldingUnitRecipe> MOULDING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "moulding_unit"), MouldingUnitRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/moulding_unit.png");

    private final IDrawable background;
    private final IDrawable icon;

    public MouldingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MOULDING_UNIT.get()));
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public RecipeType<MouldingUnitRecipe> getRecipeType() {
        return MOULDING_UNIT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.moulding_unit");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MouldingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 7)
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 55)
                .addItemStack(recipe.getResultItem(null));
    }
}
