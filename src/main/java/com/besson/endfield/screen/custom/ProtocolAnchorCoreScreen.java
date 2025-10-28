package com.besson.endfield.screen.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.util.ProtocolAnchorCoreStatus;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

// TODO: 美化GUI界面，实现类似原作游戏的GUI，环形，动态效果等
public class ProtocolAnchorCoreScreen extends AbstractContainerScreen<ProtocolAnchorCoreScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/protocol_anchor_core.png");
    private static final ResourceLocation STORAGE_TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/generic_54.png");

    public ProtocolAnchorCoreScreen(ProtocolAnchorCoreScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
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

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - 222) / 2;

        RenderSystem.setShaderTexture(0, TEXTURE);
        context.blit(TEXTURE, x - 165, y + 25, 0, 0, imageWidth, imageHeight);

        RenderSystem.setShaderTexture(0, STORAGE_TEXTURE);
        context.blit(STORAGE_TEXTURE, x, y, 0, 0, this.imageWidth, 125);
        context.blit(STORAGE_TEXTURE, x, y + 125, 0, 126, this.imageWidth, 96);

        context.drawString(this.font,
                Component.translatable("screen.protocol_core.buffer"),
                x - 95, y + 80, 0x404040, false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.storedEnergy", menu.storedEnergy),
                x - 100, y + 95, 0x404040, false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.max", 100000),
                x - 95, y + 110, 0x404040, false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.generated", menu.totalGenerated),
                x - 145, y + 159, 0xFFFFFF,false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.consumer", menu.totalDemand),
                x - 63, y + 43, 0xFF8000,false);
    }
}
