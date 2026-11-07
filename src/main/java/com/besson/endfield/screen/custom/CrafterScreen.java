package com.besson.endfield.screen.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.recipe.custom.CrafterRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;


public class CrafterScreen extends AbstractContainerScreen<CrafterScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/crafter.png");
    private static final int RECIPE_LIST_COLUMNS = 2;
    private static final int RECIPE_LIST_ROWS = 3;
    private static final int RECIPE_ENTRY_WIDTH = 16;
    private static final int RECIPE_ENTRY_HEIGHT = 18;
    private static final int VISIBLE_RECIPE_COUNT = RECIPE_LIST_COLUMNS * RECIPE_LIST_ROWS;
    private static final int RECIPE_LIST_OFFSET_X = 76;
    private static final int RECIPE_LIST_OFFSET_Y = 14;
    private static final int SCROLLBAR_X = 109;
    private static final int SCROLLBAR_Y = 15;
    private static final int SCROLLBAR_WIDTH = 4;
    private static final int SCROLLBAR_HEIGHT = 54;
    private static final int SCROLLBAR_THUMB_HEIGHT = 15;
    private static final int PANEL_COLOR = 0xFF8A8A8A;
    private static final int PANEL_BORDER_COLOR = 0xFF4E4E4E;
    private static final int RECIPE_NORMAL_COLOR = 0xFF6D6D6D;
    private static final int RECIPE_HOVER_COLOR = 0xFF9A9A9A;
    private static final int RECIPE_SELECTED_COLOR = 0xFFC8C8C8;
    private static final int RECIPE_BORDER_COLOR = 0xFF3F3F3F;

    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;
    public CrafterScreen(CrafterScreenHandler pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        menu.setContentsChangedListener(this::onInventoryChange);
        this.canCraft = pMenu.canCraft();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderRecipeList(pGuiGraphics, pMouseX, pMouseY, x + RECIPE_LIST_OFFSET_X, y + RECIPE_LIST_OFFSET_Y);
        renderScrollbar(pGuiGraphics, x, y);
    }

    @Override
    protected void renderTooltip(GuiGraphics context, int x, int y) {
        super.renderTooltip(context, x, y);
        if (!this.canCraft) {
            return;
        }

        int recipeX = this.leftPos + RECIPE_LIST_OFFSET_X;
        int recipeY = this.topPos + RECIPE_LIST_OFFSET_Y;
        List<CrafterRecipe> recipes = this.menu.getCurrentRecipes();

        for (int i = this.scrollOffset; i < this.scrollOffset + VISIBLE_RECIPE_COUNT && i < this.menu.getAvailableRecipeCount(); i++) {
            int visibleIndex = i - this.scrollOffset;
            int iconX = recipeX + visibleIndex % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int iconY = recipeY + visibleIndex / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 1;
            if (x >= iconX && x < iconX + RECIPE_ENTRY_WIDTH && y >= iconY && y < iconY + RECIPE_ENTRY_HEIGHT) {
                context.renderTooltip(this.font, recipes.get(i).getResultItem(this.minecraft.level.registryAccess()), x, y);
            }
        }
    }

    private void renderRecipeList(GuiGraphics context, int mouseX, int mouseY, int x, int y) {
        context.fill(x - 2, y - 1, x + RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH + 2, y + RECIPE_LIST_ROWS * RECIPE_ENTRY_HEIGHT + 2, PANEL_COLOR);
        context.renderOutline(x - 2, y - 1, RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH + 4, RECIPE_LIST_ROWS * RECIPE_ENTRY_HEIGHT + 3, PANEL_BORDER_COLOR);

        if (!this.canCraft) {
            return;
        }

        List<CrafterRecipe> recipes = this.menu.getCurrentRecipes();
        for (int i = this.scrollOffset; i < this.scrollOffset + VISIBLE_RECIPE_COUNT && i < this.menu.getAvailableRecipeCount(); i++) {
            int visibleIndex = i - this.scrollOffset;
            int iconX = x + visibleIndex % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int iconY = y + visibleIndex / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 1;
            boolean hovered = mouseX >= iconX && mouseX < iconX + RECIPE_ENTRY_WIDTH && mouseY >= iconY && mouseY < iconY + RECIPE_ENTRY_HEIGHT;
            int color = i == this.menu.getSelectedRecipeIndex() ? RECIPE_SELECTED_COLOR : hovered ? RECIPE_HOVER_COLOR : RECIPE_NORMAL_COLOR;

            context.fill(iconX, iconY, iconX + RECIPE_ENTRY_WIDTH, iconY + RECIPE_ENTRY_HEIGHT, color);
            context.renderOutline(iconX, iconY, RECIPE_ENTRY_WIDTH, RECIPE_ENTRY_HEIGHT, RECIPE_BORDER_COLOR);
            context.renderItem(recipes.get(i).getResultItem(this.minecraft.level.registryAccess()), iconX, iconY + 1);
        }
    }

    private void renderScrollbar(GuiGraphics context, int x, int y) {
        if (!this.shouldScroll()) {
            return;
        }

        int trackX = x + SCROLLBAR_X;
        int trackY = y + SCROLLBAR_Y;
        int thumbY = trackY + (int)((SCROLLBAR_HEIGHT - SCROLLBAR_THUMB_HEIGHT) * this.scrollAmount);

        context.fill(trackX, trackY, trackX + SCROLLBAR_WIDTH, trackY + SCROLLBAR_HEIGHT, 0xFF5A5A5A);
        context.fill(trackX, thumbY, trackX + SCROLLBAR_WIDTH, thumbY + SCROLLBAR_THUMB_HEIGHT, 0xFFD6D6D6);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int recipeX = this.leftPos + RECIPE_LIST_OFFSET_X;
            int recipeY = this.topPos + RECIPE_LIST_OFFSET_Y;

            for (int i = this.scrollOffset; i < this.scrollOffset + VISIBLE_RECIPE_COUNT && i < this.menu.getAvailableRecipeCount(); i++) {
                int visibleIndex = i - this.scrollOffset;
                double relativeX = mouseX - (recipeX + visibleIndex % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH);
                double relativeY = mouseY - (recipeY + visibleIndex / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 1);

                if (relativeX >= 0.0 && relativeY >= 0.0 && relativeX < RECIPE_ENTRY_WIDTH && relativeY < RECIPE_ENTRY_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, i)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
                    return true;
                }
            }

            int scrollbarX = this.leftPos + SCROLLBAR_X;
            int scrollbarY = this.topPos + SCROLLBAR_Y;
            if (mouseX >= scrollbarX && mouseX < scrollbarX + SCROLLBAR_WIDTH && mouseY >= scrollbarY && mouseY < scrollbarY + SCROLLBAR_HEIGHT) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int top = this.topPos + SCROLLBAR_Y;
            int bottom = top + SCROLLBAR_HEIGHT;
            this.scrollAmount = ((float)mouseY - top - SCROLLBAR_THUMB_HEIGHT / 2.0F) / (bottom - top - SCROLLBAR_THUMB_HEIGHT);
            this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollOffset = (int)(this.scrollAmount * this.getMaxScroll() + 0.5F) * RECIPE_LIST_COLUMNS;
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int maxScroll = this.getMaxScroll();
            this.scrollAmount = Mth.clamp(this.scrollAmount - (float)amount / maxScroll, 0.0F, 1.0F);
            this.scrollOffset = (int)(this.scrollAmount * maxScroll + 0.5F) * RECIPE_LIST_COLUMNS;
        }

        return true;
    }

    private boolean shouldScroll() {
        return this.canCraft && this.menu.getAvailableRecipeCount() > VISIBLE_RECIPE_COUNT;
    }

    private int getMaxScroll() {
        return (this.menu.getAvailableRecipeCount() + RECIPE_LIST_COLUMNS - 1) / RECIPE_LIST_COLUMNS - RECIPE_LIST_ROWS;
    }

    private void onInventoryChange() {
        this.canCraft = this.menu.canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0F;
            this.scrollOffset = 0;
        }
    }
}
