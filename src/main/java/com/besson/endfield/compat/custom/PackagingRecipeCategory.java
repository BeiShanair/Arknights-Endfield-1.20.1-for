package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.compat.ModItemHelper;
import com.besson.endfield.recipe.custom.PackagingUnitRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PackagingRecipeCategory implements IRecipeCategory<PackagingUnitRecipe> {
    public static final RecipeType<PackagingUnitRecipe> PACKAGING =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "packaging"), PackagingUnitRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/packaging.png");

    private final IDrawable background;
    private final IDrawable icon;

    public PackagingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.PACKAGING_UNIT.get()));
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public void draw(PackagingUnitRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }

    @Override
    public RecipeType<PackagingUnitRecipe> getRecipeType() {
        return PACKAGING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.packaging_unit");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PackagingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 18)
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput().get(0)));
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 45)
                .addItemStacks(ModItemHelper.fromInputEntries(recipe.getInput().get(1)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 31)
                .addItemStack(recipe.getResultItem(null));
    }
}
