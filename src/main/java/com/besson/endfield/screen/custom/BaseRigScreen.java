package com.besson.endfield.screen.custom;

import com.besson.endfield.blockEntity.custom.BaseRigBlockEntity;
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

public abstract class BaseRigScreen<T extends BaseRigScreenHandler<B>, B extends BaseRigBlockEntity<?>> extends AbstractContainerScreen<T> {
    protected final ResourceLocation TEXTURE = setTexture();
    protected final B entity;
    public BaseRigScreen(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.entity = (B) handler.entity;
        
    }
    
    protected abstract ResourceLocation setTexture();

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
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context,mouseX,mouseY);
    }

    protected abstract void renderProgressArrow(GuiGraphics context, int x, int y);

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(context, x, y);
    }
}
