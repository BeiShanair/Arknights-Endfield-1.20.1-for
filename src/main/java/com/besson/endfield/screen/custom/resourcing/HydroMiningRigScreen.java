package com.besson.endfield.screen.custom.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.resourcing.HydroMiningRigBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HydroMiningRigScreen extends BaseRigScreen<HydroMiningRigScreenHandler, HydroMiningRigBlockEntity> {
    private static final ResourceLocation WATER_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");
    private static final int TANK_X = 20;
    private static final int TANK_Y = 15;
    private static final int TANK_W = 18;
    private static final int TANK_H = 54;

    public HydroMiningRigScreen(HydroMiningRigScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected ResourceLocation setTexture() {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/021_no_power.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(TEXTURE, x + 68, y + 41, 176, 0, menu.getScaledProgress(), 8);
        }
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        super.renderBg(context, delta, mouseX, mouseY);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        drawWaterTank(context, x, y);
    }

    private void drawWaterTank(GuiGraphics context, int x, int y) {
        int tankX = x + TANK_X;
        int tankY = y + TANK_Y;
        
        context.fill(tankX - 1, tankY - 1, tankX + TANK_W + 1, tankY + TANK_H + 1, 0xFF3A3F44);
        context.fill(tankX, tankY, tankX + TANK_W, tankY + TANK_H, 0xFF8B8B8B);

        int amount = menu.getWaterAmount();
        int capacity = menu.getWaterCapacity();
        if (amount <= 0 || capacity <= 0) return;

        int innerX = tankX + 1;
        int innerW = TANK_W - 2;
        int innerBottom = tankY + TANK_H - 1;
        int innerH = TANK_H - 2;
        int waterH = (int) ((long) amount * innerH / capacity);
        if (waterH <= 0) return;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WATER_TEXTURE);
        RenderSystem.setShaderColor(0.2f, 0.4f, 1.0f, 0.7f);

        int drawY = innerBottom - waterH;
        while (drawY < innerBottom) {
            int h = Math.min(32, innerBottom - drawY);
            context.blit(WATER_TEXTURE, innerX, drawY, 0, 0, innerW, h, 32, 32);
            drawY += h;
        }

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        if (mouseX >= x + TANK_X && mouseX <= x + TANK_X + TANK_W
                && mouseY >= y + TANK_Y && mouseY <= y + TANK_Y + TANK_H) {

            int amount = menu.getWaterAmount();
            int capacity = menu.getWaterCapacity();

            List<Component> tooltip = new ArrayList<>();
            if (amount > 0) {
                tooltip.add(Component.translatable("tooltip.water_pump.fluid",
                        amount / 1000, capacity / 1000).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("tooltip.water_pump.empty")
                        .withStyle(ChatFormatting.GRAY));
            }
            context.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
}
