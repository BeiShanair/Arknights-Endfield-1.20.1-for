package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.GearingUnitRecipe;
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

public class GearingRecipeCategory implements IRecipeCategory<GearingUnitRecipe> {
    public static final RecipeType<GearingUnitRecipe> GEARING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "gearing_unit"), GearingUnitRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/gearing_unit.png");

    private final IDrawable background;
    private final IDrawable icon;

    public GearingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.GEARING_UNIT.get()));
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public RecipeType<GearingUnitRecipe> getRecipeType() {
        return GEARING_UNIT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.gearing_unit");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GearingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 18)
                .addIngredients(recipe.getInput().get(0).getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 45)
                .addIngredients(recipe.getInput().get(1).getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 31)
                .addItemStack(recipe.getResultItem(null));
    }
}
