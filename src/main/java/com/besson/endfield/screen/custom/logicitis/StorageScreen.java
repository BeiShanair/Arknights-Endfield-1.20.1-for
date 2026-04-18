package com.besson.endfield.screen.custom.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.network.RequestItemPacket;
import com.besson.endfield.util.storage.StorageEntry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StorageScreen extends AbstractContainerScreen<StorageScreenHandler> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/generic_54.png");
    private boolean tooltipAlreadyDrawn = false;
    public StorageScreen(StorageScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        tooltipAlreadyDrawn = false;
    
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawStorageAmounts(context);
        
        if (!tooltipAlreadyDrawn) {
            renderTooltip(context, mouseX, mouseY);
        }
    }
    @Override
    protected void renderTooltip(GuiGraphics context, int mouseX, int mouseY) {
        tooltipAlreadyDrawn = true;
    
        Slot slot = this.hoveredSlot;
        if (slot == null) {
            super.renderTooltip(context, mouseX, mouseY);
            return;
        }
    
        int index = menu.slots.indexOf(slot);
        if (index < 0) {
            super.renderTooltip(context, mouseX, mouseY);
            return;
        }
    
        int storageSlotCount = menu.getStorageSlotCount();
        if (index >= storageSlotCount) {
            super.renderTooltip(context, mouseX, mouseY);
            return;
        }
        
        List<StorageEntry> visible = menu.getVisibleEntries();
        if (index >= visible.size()) {
            super.renderTooltip(context, mouseX, mouseY);
            return;
        }
    
        StorageEntry entry = visible.get(index);
        long count = entry.getCount();
    
        List<Component> tooltip = new ArrayList<>();
        ItemStack stack = slot.getItem();
        if (!stack.isEmpty()) {
            tooltip.add(stack.getDisplayName());
        } else {
            tooltip.add(Component.literal("Unknown Item"));
        }
        
        tooltip.add(Component.literal("Num: ")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(formatExact(count))
                        .withStyle(ChatFormatting.AQUA)));
        
        context.pose().pushPose();
        context.pose().translate(0.0D, 0.0D, 600.0D); // tooltip 再往上，避免被其他 UI 遮挡
        RenderSystem.disableDepthTest();
        context.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        RenderSystem.enableDepthTest();
        context.pose().popPose();
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - 222) / 2;
        
        RenderSystem.setShaderTexture(0, TEXTURE);
        context.blit(TEXTURE, x, y, 0, 0, this.imageWidth, 125);
        context.blit(TEXTURE, x, y + 125, 0, 126, this.imageWidth, 96);
    }
    
    private void drawStorageAmounts(GuiGraphics context) {
        List<StorageEntry> visible = menu.getVisibleEntries();
        int storageSlotCount = menu.getStorageSlotCount();
        int countToDraw = Math.min(visible.size(), storageSlotCount);
        
        context.pose().pushPose();
        context.pose().translate(0.0D, 0.0D, 300.0D);
        RenderSystem.disableDepthTest();
    
        for (int i = 0; i < countToDraw; i++) {
            Slot slot = menu.slots.get(i);
            StorageEntry entry = visible.get(i);
    
            long count = entry.getCount();
            String text = format(count);

            int x = this.leftPos + slot.x + 16;
            int y = this.topPos + slot.y + 16;

            context.pose().pushPose();
            context.pose().translate(x, y, 0.0D);
            context.pose().scale(0.75f, 0.75f, 1.0f);
            context.drawString(font, text, -font.width(text), -8, 0xFFFFFF, true);
            context.pose().popPose();
        }
        
        RenderSystem.enableDepthTest();
        context.pose().popPose();
    }

    private String format(long count) {
        if (count >= 1_000_000) {
            double value = count / 1_000_000.0;
            return formatDecimal(value) + "M";
        }
        if (count >= 1_000) {
            double value = count / 1_000.0;
            return formatDecimal(value) + "k";
        }
        return String.valueOf(count);
    }
    
    private String formatDecimal(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        } else {
            return String.format("%.1f", value).replaceAll("\\.?0*$", "");
        }
    }
    
    private String formatExact(long count) {
        return String.format("%,d", count);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int maxOffset = Math.max(0, menu.getEntries().size() - menu.getStorageSlotCount());
        menu.scrollOffset = Mth.clamp(menu.scrollOffset - (int)amount * 9, 0, maxOffset);
        menu.refreshSlots();
        return true;
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int button, ClickType actionType) {
        if (slot != null) {
            int index = menu.slots.indexOf(slot);
            if (index >= menu.getVisibleEntries().size()) return;
            
            StorageEntry entry = menu.getVisibleEntries().get(index);
            requestItem(entry.getItem(), button == 1 ? 64 : 1);
        }
    }

    private void requestItem(Item item, int amount) {
        ModNetWorking.CHANNEL.sendToServer(new RequestItemPacket(item, amount));
    }
}
