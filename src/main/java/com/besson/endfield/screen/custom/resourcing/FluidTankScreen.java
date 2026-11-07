package com.besson.endfield.screen.custom.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.FluidBottleItem;
import com.besson.endfield.item.custom.MaterialBottleItem;
import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.network.TankBottleTransferPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FluidTankScreen extends AbstractContainerScreen<FluidTankScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/fluid_pump.png");
    private static final ResourceLocation WATER_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");

    public FluidTankScreen(FluidTankScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(1, TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int fluidHeight = (int) (menu.getFluidAmount() / (float) menu.getFluidCapacity() * 64);
        if (fluidHeight > 0) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // 按罐内流体类型取色（与水泵 GUI 共用同一套 tint 逻辑）
            Fluid fluid = BuiltInRegistries.FLUID.byId(menu.getFluidId());
            FluidPumpScreen.applyFluidColor(fluid);

            RenderSystem.setShaderTexture(0, WATER_TEXTURE);

            int fluidX = x + 64;
            int fluidY = y + 75 - fluidHeight;
            for (int yOffset = 0; yOffset < fluidHeight; yOffset += 32) {
                int drawHeight = Math.min(32, fluidHeight - yOffset);
                context.blit(WATER_TEXTURE, fluidX, fluidY + yOffset,
                        0, 0, 48, drawHeight, 32, 32);
            }

            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context,mouseX,mouseY);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int fluidX = x + 64;
        int fluidY = y + 11;
        int fluidWidth = 48;
        int fluidHeight = 64;

        if (mouseX >= fluidX && mouseX <= fluidX + fluidWidth &&
                mouseY >= fluidY && mouseY <= fluidY + fluidHeight) {

            int amount = menu.getFluidAmount();
            int capacity = menu.getFluidCapacity();

            List<Component> tooltip = new ArrayList<>();
            if (amount > 0) {
                Fluid fluid = BuiltInRegistries.FLUID.byId(menu.getFluidId());
                Component fluidName = new FluidStack(fluid, 1000).getDisplayName();
                tooltip.add(Component.translatable("tooltip.water_pump.fluid_type",
                        fluidName, amount / 1000, capacity / 1000).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("tooltip.water_pump.empty")
                        .withStyle(ChatFormatting.GRAY));
            }

            context.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int fluidX = x + 64;
        int fluidY = y + 11;
        int fluidWidth = 48;
        int fluidHeight = 64;

        if (mouseX >= fluidX && mouseX <= fluidX + fluidWidth
                && mouseY >= fluidY && mouseY <= fluidY + fluidHeight) {
            ItemStack cursor = this.menu.getCarried();
            if (cursor.getItem() instanceof FluidBottleItem || cursor.getItem() instanceof MaterialBottleItem) {
                ModNetWorking.CHANNEL.sendToServer(
                        new TankBottleTransferPacket(this.menu.getBlockEntity().getBlockPos()));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
