package com.besson.endfield.screen.custom.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.logicitis.ProtocolStashBlockEntity;
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

public class ProtocolStashScreen extends AbstractContainerScreen<ProtocolStashScreenHandler> {
    private static final ResourceLocation STORAGE_TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/generic_54.png");
    private final ProtocolStashBlockEntity entity;
    public ProtocolStashScreen(ProtocolStashScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.entity = (ProtocolStashBlockEntity) handler.entity;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ToggleIconButton(leftPos + 150, topPos + 1, menu::isEnabled,
                button -> {
                    boolean newEnableState = !menu.isEnabled();
                    menu.setEnabled(newEnableState);
                    ModNetWorking.CHANNEL.sendToServer(
                            new SwitchPacket(entity.getBlockPos(), newEnableState)
                    );
                }));
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        
        RenderSystem.setShaderTexture(0, STORAGE_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        context.blit(STORAGE_TEXTURE, i, j, 0, 0, this.imageWidth, 3 * 18 + 17);
        context.blit(STORAGE_TEXTURE, i, j + 3 * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
