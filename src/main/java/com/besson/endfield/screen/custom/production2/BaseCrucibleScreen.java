package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleBlockEntity;
import com.besson.endfield.network.CrucibleClearOutputPacket;
import com.besson.endfield.network.CrucibleSetOutputPacket;
import com.besson.endfield.network.ModNetWorking;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseCrucibleScreen<H extends BaseCrucibleScreenHandler> extends AbstractContainerScreen<H> {
    private static final ResourceLocation WATER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");

    private static final Component ITEM_OUTPUT_LABEL = Component.translatable("tooltip.crucible.item_output");
    private static final Component FLUID_OUTPUT_LABEL = Component.translatable("tooltip.crucible.fluid_output");
    private static final Component UNSET_LABEL = Component.translatable("tooltip.crucible.unset");
    private static final Component CLICK_CLEAR_LABEL = Component.translatable("tooltip.crucible.click_clear");

    protected static final int ITEM_OUTPUT_X = 79;
    protected static final int FLUID_OUTPUT_X_0 = 47;
    protected static final int FLUID_OUTPUT_X_1 = 111;
    protected static final int OUTPUT_Y = 127;
    protected static final int SLOT_SIZE = 16;

    protected BaseCrucibleScreen(H handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelY = -1000;
        this.inventoryLabelY = -1000;
    }

    protected abstract ResourceLocation getTexture();

    protected abstract int[] getInputX();

    protected abstract int[] getInputY();

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        graphics.blit(getTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight);

        BaseCrucibleBlockEntity be = menu.getBlockEntity();
        int progress = be.getProgress();
        int maxProgress = be.getMaxProgress();
        int progressWidth = maxProgress <= 0 ? 0 : (int) (48 * (progress / (float) maxProgress));
        graphics.fill(leftPos + 64, topPos + 110, leftPos + 112, topPos + 114, 0xFF14171B);
        graphics.fill(leftPos + 64, topPos + 110, leftPos + 64 + progressWidth, topPos + 114, 0xFF72D0B2);

        drawResources(graphics);
    }

    private void drawResources(GuiGraphics graphics) {
        BaseCrucibleBlockEntity be = menu.getBlockEntity();
        List<BaseCrucibleBlockEntity.StoredResource> resources = be.getDisplayResources();
        int[] inputX = getInputX();
        int[] inputY = getInputY();
        for (int i = 0; i < resources.size() && i < inputX.length; i++) {
            BaseCrucibleBlockEntity.StoredResource resource = resources.get(i);
            int slotX = leftPos + inputX[i] + 1;
            int slotY = topPos + inputY[i] + 1;
            if (resource.item()) {
                ItemStack stack = resource.asStack();
                graphics.renderItem(stack, slotX, slotY);
                graphics.renderItemDecorations(font, stack, slotX, slotY, formatAmount(resource.amount()));
            } else {
                drawInputFluidCell(graphics, slotX, slotY, resource.fluidStack().getFluid(), resource.amount());
            }
        }

        ItemStack itemFilter = be.getItemOutputFilter();
        if (!itemFilter.isEmpty()) {
            graphics.renderItem(itemFilter, leftPos + ITEM_OUTPUT_X + 1, topPos + OUTPUT_Y + 1);
        }
        drawFluidOutput(graphics, leftPos + FLUID_OUTPUT_X_0 + 1, topPos + OUTPUT_Y + 1, 0);
        drawFluidOutput(graphics, leftPos + FLUID_OUTPUT_X_1 + 1, topPos + OUTPUT_Y + 1, 1);
    }

    private void drawFluidOutput(GuiGraphics graphics, int x, int y, int index) {
        FluidStack filter = menu.getBlockEntity().getFluidOutputFilter(index);
        if (!filter.isEmpty()) {
            drawFluidTexture(graphics, x, y, SLOT_SIZE, SLOT_SIZE, filter.getFluid());
        }
    }
    
    private void drawInputFluidCell(GuiGraphics graphics, int x, int y, Fluid fluid, long amount) {
        long capacity = BaseCrucibleBlockEntity.MAX_FLUID_AMOUNT;
        int fluidHeight = capacity <= 0 ? 0 : (int) (amount / (float) capacity * SLOT_SIZE);
        if (fluidHeight <= 0) return;
        drawFluidTexture(graphics, x, y + SLOT_SIZE - fluidHeight, SLOT_SIZE, fluidHeight, fluid);
    }
    
    private void drawFluidTexture(GuiGraphics graphics, int x, int y, int width, int height, Fluid fluid) {
        int tint = IClientFluidTypeExtensions.of(fluid).getTintColor();
        float r = ((tint >> 16) & 0xFF) / 255.0f;
        float g = ((tint >> 8) & 0xFF) / 255.0f;
        float b = (tint & 0xFF) / 255.0f;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WATER_TEXTURE);
        RenderSystem.setShaderColor(r, g, b, 1.0f);
        graphics.blit(WATER_TEXTURE, x, y, 0, 0, width, height, 16, 16);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int[] inputX = getInputX();
            int[] inputY = getInputY();
            for (int i = 0; i < inputX.length; i++) {
                if (isIn(mouseX, mouseY, leftPos + inputX[i], topPos + inputY[i], SLOT_SIZE, SLOT_SIZE)) {
                    ModNetWorking.CHANNEL.sendToServer(
                            new CrucibleSetOutputPacket(menu.getBlockEntity().getBlockPos(), i));
                    return true;
                }
            }
            
            if (isIn(mouseX, mouseY, leftPos + ITEM_OUTPUT_X, topPos + OUTPUT_Y, SLOT_SIZE, SLOT_SIZE)
                    && !menu.getBlockEntity().getItemOutputFilter().isEmpty()) {
                sendClearOutput(0);
                return true;
            }
            if (isIn(mouseX, mouseY, leftPos + FLUID_OUTPUT_X_0, topPos + OUTPUT_Y, SLOT_SIZE, SLOT_SIZE)
                    && !menu.getBlockEntity().getFluidOutputFilter(0).isEmpty()) {
                sendClearOutput(1);
                return true;
            }
            if (isIn(mouseX, mouseY, leftPos + FLUID_OUTPUT_X_1, topPos + OUTPUT_Y, SLOT_SIZE, SLOT_SIZE)
                    && !menu.getBlockEntity().getFluidOutputFilter(1).isEmpty()) {
                sendClearOutput(2);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void sendClearOutput(int index) {
        ModNetWorking.CHANNEL.sendToServer(
                new CrucibleClearOutputPacket(menu.getBlockEntity().getBlockPos(), index));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
        drawCustomTooltips(graphics, mouseX, mouseY);
    }

    private void drawCustomTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        List<BaseCrucibleBlockEntity.StoredResource> resources = menu.getBlockEntity().getDisplayResources();
        int[] inputX = getInputX();
        int[] inputY = getInputY();
        for (int i = 0; i < resources.size() && i < inputX.length; i++) {
            if (isIn(mouseX, mouseY, leftPos + inputX[i], topPos + inputY[i], SLOT_SIZE, SLOT_SIZE)) {
                graphics.renderTooltip(font, getTooltip(resources.get(i)), Optional.empty(), mouseX, mouseY);
                return;
            }
        }

        if (isIn(mouseX, mouseY, leftPos + ITEM_OUTPUT_X, topPos + OUTPUT_Y, SLOT_SIZE, SLOT_SIZE)) {
            graphics.renderTooltip(font, itemOutputTooltip(menu.getBlockEntity().getItemOutputFilter()),
                    Optional.empty(), mouseX, mouseY);
            return;
        }
        if (isIn(mouseX, mouseY, leftPos + FLUID_OUTPUT_X_0, topPos + OUTPUT_Y, SLOT_SIZE, SLOT_SIZE)) {
            graphics.renderTooltip(font, fluidOutputTooltip(menu.getBlockEntity().getFluidOutputFilter(0)),
                    Optional.empty(), mouseX, mouseY);
            return;
        }
        if (isIn(mouseX, mouseY, leftPos + FLUID_OUTPUT_X_1, topPos + OUTPUT_Y, SLOT_SIZE, SLOT_SIZE)) {
            graphics.renderTooltip(font, fluidOutputTooltip(menu.getBlockEntity().getFluidOutputFilter(1)),
                    Optional.empty(), mouseX, mouseY);
        }
    }

    private List<Component> getTooltip(BaseCrucibleBlockEntity.StoredResource resource) {
        List<Component> tooltip = new ArrayList<>();
        if (resource.item()) {
            tooltip.add(resource.itemStack().getHoverName());
            tooltip.add(Component.literal(String.valueOf(resource.amount())).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(resource.fluidStack().getDisplayName());
            tooltip.add(Component.literal(resource.amount() + " mB").withStyle(ChatFormatting.GRAY));
        }
        return tooltip;
    }
    
    private List<Component> itemOutputTooltip(ItemStack filter) {
        List<Component> tooltip = new ArrayList<>();
        if (filter.isEmpty()) {
            tooltip.add(ITEM_OUTPUT_LABEL.copy().withStyle(ChatFormatting.YELLOW));
            tooltip.add(UNSET_LABEL.copy().withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(filter.getHoverName());
            tooltip.add(ITEM_OUTPUT_LABEL.copy().withStyle(ChatFormatting.YELLOW));
            tooltip.add(CLICK_CLEAR_LABEL.copy().withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        return tooltip;
    }
    
    private List<Component> fluidOutputTooltip(FluidStack filter) {
        List<Component> tooltip = new ArrayList<>();
        if (filter.isEmpty()) {
            tooltip.add(FLUID_OUTPUT_LABEL.copy().withStyle(ChatFormatting.YELLOW));
            tooltip.add(UNSET_LABEL.copy().withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(filter.getDisplayName());
            tooltip.add(FLUID_OUTPUT_LABEL.copy().withStyle(ChatFormatting.YELLOW));
            tooltip.add(CLICK_CLEAR_LABEL.copy().withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        return tooltip;
    }

    private boolean isIn(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private String formatAmount(long amount) {
        return amount > 999 ? amount / 1000 + "k" : String.valueOf(amount);
    }
}
