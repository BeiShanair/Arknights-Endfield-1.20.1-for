package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.recipe.custom.CrafterRecipe;
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
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CrafterRecipeCategory implements IRecipeCategory<CrafterRecipe> {
    public static final RecipeType<CrafterRecipe> CRAFTER =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "crafter"), CrafterRecipe.class);
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/crafter.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CrafterRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 4, 168, 76);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CRAFTER.get()));
    }

    @Override
    public RecipeType<CrafterRecipe> getRecipeType() {
        return CRAFTER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("blockEntity.crafter");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrafterRecipe recipe, IFocusGroup focuses) {
        int startX = 26;
        int startY = 31;
        int spacing = 18;
        int index = 0;
        for (Map.Entry<ItemLike, Integer> entry : recipe.getRequired().entrySet()) {
            if (index > 3) break;

            int x = startX + (index * spacing);
            int y = startY;

            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(entry.getKey().asItem(), entry.getValue()));
            index++;
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 31)
                .addItemStack(recipe.getResultItem(null));
    }
}
