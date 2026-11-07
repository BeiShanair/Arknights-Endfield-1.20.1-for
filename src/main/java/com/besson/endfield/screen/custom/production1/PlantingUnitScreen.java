package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production1.PlantingUnitBlockEntity;
import com.besson.endfield.network.FluidModeChangePacket;
import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.screen.FluidModeButton;
import com.besson.endfield.screen.custom.BaseIOScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlantingUnitScreen extends BaseIOScreen<PlantingUnitScreenHandler, PlantingUnitBlockEntity> {
    private static final ResourceLocation WATER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");
    private static final int FLUID_BAR_X = 40;
    private static final int FLUID_BAR_Y = 11;
    private static final int FLUID_BAR_WIDTH = 16;
    private static final int FLUID_BAR_HEIGHT = 64;

    private static final int POWER_BAR_X = 158;
    private static final int POWER_BAR_Y = 11;
    private static final int POWER_BAR_WIDTH = 8;
    private static final int POWER_BAR_HEIGHT = 64;

    public PlantingUnitScreen(PlantingUnitScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new FluidModeButton(this.font, leftPos + 140, topPos + 30, menu::nowFluidMode,
                button -> ModNetWorking.CHANNEL.sendToServer(
                        new FluidModeChangePacket(entity.getBlockPos())
                )));
    }

    @Override
    protected ResourceLocation setTexture() {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/121.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress());
        }
        renderFluidBar(context, x, y);
        renderStoredPowerBar(context, x, y);
    }

    private void renderStoredPowerBar(GuiGraphics context, int x, int y) {
        int storedPower = menu.getStoredPower();
        int powerHeight = (int) ((10000F - storedPower) / 10000f * POWER_BAR_HEIGHT);
        if (powerHeight <= 0) return;
        context.blit(TEXTURE, x + POWER_BAR_X, y + POWER_BAR_Y, 187, 0, POWER_BAR_WIDTH, powerHeight);
    }

    private void renderFluidBar(GuiGraphics context, int x, int y) {
        int fluidX = x + FLUID_BAR_X;
        int fluidY = y + FLUID_BAR_Y;
        
        if (menu.nowFluidMode()) {
            context.fill(fluidX - 1, fluidY - 1, fluidX + FLUID_BAR_WIDTH + 1, fluidY + FLUID_BAR_HEIGHT + 1, 0xFF3A3F44);
            context.fill(fluidX, fluidY, fluidX + FLUID_BAR_WIDTH, fluidY + FLUID_BAR_HEIGHT, 0xFF8b8b8b);
        }

        int capacity = menu.getFluidCapacity();
        if (capacity <= 0) return;

        int amount = menu.getFluidAmount();
        
        int innerX = fluidX + 1;
        int innerW = FLUID_BAR_WIDTH - 2;
        int innerBottom = fluidY + FLUID_BAR_HEIGHT - 1;
        int innerH = FLUID_BAR_HEIGHT - 2;
        int innerFluidHeight = (int) (amount / (float) capacity * innerH);
        if (innerFluidHeight <= 0) return;
        int drawY = innerBottom - innerFluidHeight;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        applyFluidColor(BuiltInRegistries.FLUID.byId(menu.getFluidId()));

        for (int yOffset = 0; yOffset < innerFluidHeight; yOffset += 16) {
            int drawHeight = Math.min(16, innerFluidHeight - yOffset);
            context.blit(WATER_TEXTURE, innerX, drawY + yOffset, 0, 0, innerW, drawHeight, 16, 16);
        }

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
    
    private static void applyFluidColor(Fluid fluid) {
        Fluid actual = fluid == null ? Fluids.WATER : fluid;
        int tint = IClientFluidTypeExtensions.of(actual).getTintColor();
        RenderSystem.setShaderColor(((tint >> 16) & 0xFF) / 255f, ((tint >> 8) & 0xFF) / 255f,
                (tint & 0xFF) / 255f, 0.7f);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        if (!menu.nowFluidMode()) return;

        int bgX = (this.width - this.imageWidth) / 2;
        int bgY = (this.height - this.imageHeight) / 2;
        
        int fluidX = bgX + FLUID_BAR_X;
        int fluidY = bgY + FLUID_BAR_Y;
        if (mouseX >= fluidX && mouseX <= fluidX + FLUID_BAR_WIDTH &&
                mouseY >= fluidY && mouseY <= fluidY + FLUID_BAR_HEIGHT) {
            int amount = menu.getFluidAmount();
            int capacity = menu.getFluidCapacity();

            List<Component> tooltip = new ArrayList<>();
            if (amount > 0) {
                tooltip.add(Component.translatable("tooltip.planting_unit.tank",
                        amount / 1000, capacity / 1000).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("tooltip.planting_unit.tank_empty")
                        .withStyle(ChatFormatting.GRAY));
            }
            context.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
}
