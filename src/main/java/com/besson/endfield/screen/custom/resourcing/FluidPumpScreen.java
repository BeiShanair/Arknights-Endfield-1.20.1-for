package com.besson.endfield.screen.custom.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.network.FillBucketFromPumpPacket;
import com.besson.endfield.network.ModNetWorking;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FluidPumpScreen extends AbstractContainerScreen<FluidPumpScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/fluid_pump.png");
    private static final ResourceLocation WATER_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");
    private static final int FLUID_X = 64;
    private static final int FLUID_Y = 11;
    private static final int FLUID_WIDTH = 48;
    private static final int FLUID_HEIGHT = 64;
    
    private static final int POWER_BAR_X = 158;
    private static final int POWER_BAR_Y = 11;
    private static final int POWER_BAR_WIDTH = 8;
    private static final int POWER_BAR_HEIGHT = 64;
    private static final int MAX_STORED_POWER = 10000;

    public FluidPumpScreen(FluidPumpScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int capacity = menu.getFluidCapacity();
        int fluidHeight = capacity > 0 ? (int) (menu.getFluidAmount() / (float) capacity * FLUID_HEIGHT) : 0;
        if (fluidHeight > 0) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Fluid fluid = BuiltInRegistries.FLUID.byId(menu.getFluidId());
            applyFluidColor(fluid);

            RenderSystem.setShaderTexture(0, WATER_TEXTURE);
            int fluidX = x + FLUID_X;
            int fluidY = y + FLUID_Y + FLUID_HEIGHT - fluidHeight;
            for (int yOffset = 0; yOffset < fluidHeight; yOffset += 32) {
                int drawHeight = Math.min(32, fluidHeight - yOffset);
                guiGraphics.blit(WATER_TEXTURE, fluidX, fluidY + yOffset,
                        0, 0, FLUID_WIDTH, drawHeight, 32, 32);
            }

            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }
        
        int storedPower = menu.getStoredPower();
        int coverHeight = (int) ((MAX_STORED_POWER - storedPower) / (float) MAX_STORED_POWER * POWER_BAR_HEIGHT);
        if (coverHeight > 0) {
            guiGraphics.blit(TEXTURE, x + POWER_BAR_X, y + POWER_BAR_Y, 204, 0, POWER_BAR_WIDTH, coverHeight);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (isHovering(FLUID_X, FLUID_Y, FLUID_WIDTH, FLUID_HEIGHT, mouseX, mouseY)) {
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
            guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }

        int powerX = x + POWER_BAR_X;
        int powerY = y + POWER_BAR_Y;
        if (mouseX >= powerX && mouseX <= powerX + POWER_BAR_WIDTH
                && mouseY >= powerY && mouseY <= powerY + POWER_BAR_HEIGHT) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("tooltip.fluid_pump.stored_power", menu.getStoredPower())
                    .withStyle(ChatFormatting.YELLOW));
            guiGraphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int fluidX = x + FLUID_X;
        int fluidY = y + FLUID_Y;

        if (mouseX >= fluidX && mouseX <= fluidX + FLUID_WIDTH
                && mouseY >= fluidY && mouseY <= fluidY + FLUID_HEIGHT) {
            if (minecraft != null && minecraft.player != null
                    && minecraft.player.containerMenu.getCarried().getItem() == Items.BUCKET) {
                ModNetWorking.CHANNEL.sendToServer(new FillBucketFromPumpPacket(menu.getBlockEntityPos()));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    static void applyFluidColor(Fluid fluid) {
        Fluid actual = fluid == null ? Fluids.WATER : fluid;
        int tint = IClientFluidTypeExtensions.of(actual).getTintColor();
        RenderSystem.setShaderColor(((tint >> 16) & 0xFF) / 255f, ((tint >> 8) & 0xFF) / 255f,
                (tint & 0xFF) / 255f, 0.7f);
    }
}
