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

    private final BlockPos pos;

    public ProtocolAnchorCoreScreen(ProtocolAnchorCoreScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.pos = handler.getPos();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        context.drawString(this.font,
                Component.translatable("screen.protocol_core.buffer", menu.storedEnergy, 100000),
                x + 8, y + 20, 0x404040, false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.generated", menu.totalGenerated),
                x + 8, y + 30, 0xFFFFFF,false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.consumer", menu.totalDemand),
                x + 8, y + 40, 0xFF8000,false);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }
}
