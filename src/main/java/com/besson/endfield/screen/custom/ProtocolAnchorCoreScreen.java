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

    private void drawBackground(GuiGraphics context, float delta, int mouseX, int mouseY, ProtocolAnchorCoreStatus status) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 进度条
        int barWidth = 120;
        int barHeight = 12;
        int filled = (int) ((double) status.buffer() / status.maxBuffer() * barWidth);

        context.fill(x + 25, y + 120, x + 25 + barWidth, y + 120  + barHeight, 0xFF555555); // 背景
        context.fill(x + 25, y + 120, x + 25 + filled, y + 120 + barHeight, 0xFF2E7D32);   // 电量
        context.renderOutline(x + 25, y + 120, barWidth, barHeight, 0xFF000000); // 边框

        // 数值文字（核心缓冲）
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.buffer", status.buffer(), status.maxBuffer()),
                x + 8, y + 20, 0x404040, false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.base_power", status.basePower()),
                x + 8, y + 30, 0xFFFFFF,false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.extra_power", status.extraPower()),
                x + 8, y + 40, 0xFF8000,false);
        context.drawString(this.font,
                Component.translatable("screen.protocol_core.load", status.loadNodeNum()),
                x + 8, y + 50, 0xFF0000,false);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        drawBackground(context, delta, mouseX, mouseY, menu.getStatus());
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }
}
