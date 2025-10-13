package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.SeedPickingUnitRecipe;
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

public class SeedPickingRecipeCategory implements IRecipeCategory<SeedPickingUnitRecipe> {
    public static final RecipeType<SeedPickingUnitRecipe> SEED_PICKING_UNIT =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "seed_picking_unit"), SeedPickingUnitRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/seed_picking_unit.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SeedPickingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SEED_PICKING_UNIT.get()));
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public RecipeType<SeedPickingUnitRecipe> getRecipeType() {
        return SEED_PICKING_UNIT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.seed_picking_unit");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SeedPickingUnitRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 7)
                .addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 55)
                .addItemStack(recipe.getResultItem(null));
    }
}
