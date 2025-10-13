package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.OreRigRecipe;
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

public class ElectricMiningRigMkIIRecipeCategory implements IRecipeCategory<OreRigRecipe> {
    public static final RecipeType<OreRigRecipe> ELECTRIC_MINING_RIG_MK_II =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "electric_mining_rig_mk_ii"), OreRigRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/electric_mining_rig_mk_ii.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ElectricMiningRigMkIIRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ELECTRIC_MINING_RIG_MK_II.get()));
    }

    @Override
    public RecipeType<OreRigRecipe> getRecipeType() {
        return ELECTRIC_MINING_RIG_MK_II;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.electric_mining_rig_mk_ii");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OreRigRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 33)
                .addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 33)
                .addItemStack(recipe.getResultItem(null));
    }
}
