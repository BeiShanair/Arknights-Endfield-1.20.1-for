package com.besson.endfield.compat.custom;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachineRecipeCategory<T> implements IRecipeCategory<T> {

    protected static final int SLOT_SIZE = 18;
    protected static final int PITCH = 20;
    protected static final int GAP = 8;
    protected static final int ARROW_WIDTH = 22;
    protected static final int ARROW_HEIGHT = 16;

    protected static final int WIDTH = 168;
    protected static final int HEIGHT = 54;
    protected static final int SLOT_Y = (HEIGHT - SLOT_SIZE) / 2;
    protected static final int ARROW_Y = SLOT_Y + (SLOT_SIZE - ARROW_HEIGHT) / 2;

    private final RecipeType<T> recipeType;
    private final Component title;
    private final IDrawable icon;

    protected AbstractMachineRecipeCategory(RecipeType<T> recipeType, String titleKey,
                                           IGuiHelper helper, ItemLike iconItem) {
        this.recipeType = recipeType;
        this.title = Component.translatable(titleKey);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(iconItem));
    }
    @Override
    public final RecipeType<T> getRecipeType() {
        return recipeType;
    }

    @Override
    public final Component getTitle() {
        return title;
    }

    @Override
    public final @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public final int getWidth() {
        return WIDTH;
    }

    @Override
    public final int getHeight() {
        return HEIGHT;
    }

    protected abstract int inputSlotCount(T recipe);

    protected int outputSlotCount(T recipe) {
        return 1;
    }

    protected int maxSlots() {
        return 3;
    }

    protected final int slots(int count) {
        return Math.max(1, Math.min(count, maxSlots()));
    }

    protected final int inputStartX(int inputs, int outputs) {
        return (WIDTH - contentWidth(slots(inputs), slots(outputs))) / 2;
    }

    protected final int outputStartX(int inputs, int outputs) {
        return arrowX(inputs, outputs) + ARROW_WIDTH + GAP;
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, T recipe, IFocusGroup focuses) {
        int inputs = inputSlotCount(recipe);
        int outputs = outputSlotCount(recipe);
        builder.addRecipeArrow().setPosition(arrowX(inputs, outputs), ARROW_Y);
    }

    private int arrowX(int inputs, int outputs) {
        return inputStartX(inputs, outputs) + groupWidth(slots(inputs)) + GAP;
    }

    private static int groupWidth(int slotCount) {
        return slotCount * SLOT_SIZE + (slotCount - 1) * (PITCH - SLOT_SIZE);
    }

    private static int contentWidth(int inputs, int outputs) {
        return groupWidth(inputs) + GAP + ARROW_WIDTH + GAP + groupWidth(outputs);
    }
}
