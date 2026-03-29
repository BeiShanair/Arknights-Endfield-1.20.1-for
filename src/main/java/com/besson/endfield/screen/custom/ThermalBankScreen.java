package com.besson.endfield.screen.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.powering.ThermalBankBlockEntity;
import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.network.SwitchPacket;
import com.besson.endfield.screen.ToggleIconButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ThermalBankScreen extends AbstractContainerScreen<ThermalBankScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/thermal_bank.png");
    private final ThermalBankBlockEntity entity;
    public ThermalBankScreen(ThermalBankScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.entity = handler.entity;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ToggleIconButton(leftPos + 150, topPos + 30, menu::isEnabled,
                button -> {
                    boolean newEnableState = !menu.isEnabled();
                    menu.setEnabled(newEnableState);
                    ModNetWorking.CHANNEL.sendToServer(
                            new SwitchPacket(entity.getBlockPos(), newEnableState)
                    );
                }));
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressFire(context, x, y);
    }

    private void renderProgressFire(GuiGraphics context, int x, int y) {
        if (menu.isBurning()){
            int totalFireHeight = 14;
            int fireHeight = menu.getScaledProgress();
            int fireYOffset = totalFireHeight - fireHeight;

            context.blit(TEXTURE,x + 80, y + 39 + fireYOffset, 176, fireYOffset, 14, fireHeight);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context,mouseX,mouseY);
    }
}
