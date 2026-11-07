package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production2.SeparatingUnitBlockEntity;
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

public class SeparatingUnitScreen extends BaseIOScreen<SeparatingUnitScreenHandler, SeparatingUnitBlockEntity> {
    private static final ResourceLocation WATER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");
    private static final int FLUID_BAR_X = 120;
    private static final int FLUID_BAR_Y = 11;
    private static final int FLUID_BAR_WIDTH = 16;
    private static final int FLUID_BAR_HEIGHT = 64;

    public SeparatingUnitScreen(SeparatingUnitScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected ResourceLocation setTexture() {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/121.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics graphics, int x, int y) {
        if (menu.isCrafting()) {
            graphics.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress());
        }

        renderFluidBar(graphics, x, y);
        renderStoredPowerBar(graphics, x, y);
    }

    private void renderStoredPowerBar(GuiGraphics graphics, int x, int y) {
        int storedPower = menu.getStoredPower();
        int powerHeight = (int) ((10000F - storedPower) / 10000f * 64);
        if (powerHeight <= 0) return;
        graphics.blit(TEXTURE, x + 158, y + 11, 187, 0, 8, powerHeight);
    }

    private void renderFluidBar(GuiGraphics graphics, int x, int y) {
        int fluidX = x + FLUID_BAR_X;
        int fluidY = y + FLUID_BAR_Y;
        
        graphics.fill(fluidX - 1, fluidY - 1, fluidX + FLUID_BAR_WIDTH + 1, fluidY + FLUID_BAR_HEIGHT + 1, 0xFF3A3F44);
        graphics.fill(fluidX, fluidY, fluidX + FLUID_BAR_WIDTH, fluidY + FLUID_BAR_HEIGHT, 0xFF8b8b8b);

        int capacity = menu.getFluidCapacity();
        if (capacity <= 0) return;

        int amount = menu.getFluidAmount();
        if (amount <= 0) return;
        
        int innerX = fluidX + 1;
        int innerW = FLUID_BAR_WIDTH - 2;
        int innerBottom = fluidY + FLUID_BAR_HEIGHT - 1;
        int innerH = FLUID_BAR_HEIGHT - 2;
        int innerFluidHeight = (int) (amount / (float) capacity * innerH);
        if (innerFluidHeight <= 0) return;
        int drawY = innerBottom - innerFluidHeight;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        applyFluidColor(BuiltInRegistries.FLUID.byId(menu.getFluidRawId()));

        for (int yOffset = 0; yOffset < innerFluidHeight; yOffset += 16) {
            int drawHeight = Math.min(16, innerFluidHeight - yOffset);
            graphics.blit(WATER_TEXTURE, innerX, drawY + yOffset, 0, 0, innerW, drawHeight, 16, 16);
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        int fluidX = x + FLUID_BAR_X;
        int fluidY = y + FLUID_BAR_Y;
        if (mouseX >= fluidX && mouseX <= fluidX + FLUID_BAR_WIDTH
                && mouseY >= fluidY && mouseY <= fluidY + FLUID_BAR_HEIGHT) {
            int amount = menu.getFluidAmount();
            int capacity = menu.getFluidCapacity();

            List<Component> tooltip = new ArrayList<>();
            if (amount > 0) {
                Fluid fluid = BuiltInRegistries.FLUID.byId(menu.getFluidRawId());
                Component fluidName = fluid == null ? Component.empty() : fluid.getFluidType().getDescription();
                tooltip.add(Component.translatable("tooltip.separating_unit.fluid",
                        fluidName, amount / 1000, capacity / 1000).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("tooltip.separating_unit.fluid_empty")
                        .withStyle(ChatFormatting.GRAY));
            }
            graphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
        
        int powerX = x + 158;
        int powerY = y + 11;
        if (mouseX >= powerX && mouseX <= powerX + 8
                && mouseY >= powerY && mouseY <= powerY + 64) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("tooltip.separating_unit.stored_power",
                    menu.getStoredPower()).withStyle(ChatFormatting.YELLOW));
            graphics.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
}
