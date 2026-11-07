package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production1.WaterTreatmentUnitBlockEntity;
import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.network.SwitchPacket;
import com.besson.endfield.screen.ToggleIconButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WaterTreatmentUnitScreen extends AbstractContainerScreen<WaterTreatmentUnitScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/120.png");
    private static final ResourceLocation WATER_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");

    private static final int FLUID_BAR_X = 40;
    private static final int FLUID_BAR_Y = 37;
    private static final int FLUID_BAR_WIDTH = 16;
    private static final int FLUID_BAR_HEIGHT = 16;

    private static final int POWER_BAR_X = 158;
    private static final int POWER_BAR_Y = 11;
    private static final int POWER_BAR_WIDTH = 8;
    private static final int POWER_BAR_HEIGHT = 64;
    private static final int MAX_STORED_POWER = 10000;

    private final WaterTreatmentUnitBlockEntity entity;

    public WaterTreatmentUnitScreen(WaterTreatmentUnitScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.entity = handler.entity;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ToggleIconButton(leftPos + 140, topPos + 10, menu::isEnabled,
                button -> {
                    boolean newEnableState = !menu.isEnabled();
                    menu.setEnabled(newEnableState);
                    ModNetWorking.CHANNEL.sendToServer(new SwitchPacket(entity.getBlockPos(), newEnableState));
                }));
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        context.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        renderProgressArrow(context, x, y);
        renderFluidBar(context, x, y);
        renderStoredPowerBar(context, x, y);
    }

    private void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(TEXTURE, x + 68, y + 41, 176, 0, menu.getScaledProgress(), 8);
        }
    }
    
    private void renderFluidBar(GuiGraphics context, int x, int y) {
        int capacity = menu.getFluidCapacity();
        if (capacity <= 0) return;

        int amount = menu.getFluidAmount();
        int fluidHeight = (int) (amount / (float) capacity * FLUID_BAR_HEIGHT);
        if (fluidHeight <= 0) return;
        int drawY = y + FLUID_BAR_Y + FLUID_BAR_HEIGHT - fluidHeight;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Fluid fluid = BuiltInRegistries.FLUID.byId(menu.getFluidId());
        applyFluidColor(fluid);
        RenderSystem.setShaderTexture(0, WATER_TEXTURE);

        context.blit(WATER_TEXTURE, x + FLUID_BAR_X, drawY,
                0, 0, FLUID_BAR_WIDTH, fluidHeight, 16, 16);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
    
    private void renderStoredPowerBar(GuiGraphics context, int x, int y) {
        int storedPower = menu.getStoredPower();
        int powerHeight = (int) ((MAX_STORED_POWER - storedPower) / (float) MAX_STORED_POWER * POWER_BAR_HEIGHT);
        if (powerHeight <= 0) return;
        context.blit(TEXTURE, x + POWER_BAR_X, y + POWER_BAR_Y, 204, 0, POWER_BAR_WIDTH, powerHeight);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        renderFluidTooltip(context, x, y, mouseX, mouseY);
        renderPowerTooltip(context, x, y, mouseX, mouseY);
    }

    private void renderFluidTooltip(GuiGraphics context, int x, int y, int mouseX, int mouseY) {
        int fluidX = x + FLUID_BAR_X;
        int fluidY = y + FLUID_BAR_Y;
        if (mouseX >= fluidX && mouseX <= fluidX + FLUID_BAR_WIDTH
                && mouseY >= fluidY && mouseY <= fluidY + FLUID_BAR_HEIGHT) {
            int amount = menu.getFluidAmount();
            int capacity = menu.getFluidCapacity();

            List<Component> tooltip = new ArrayList<>();
            if (amount > 0) {
                Fluid fluid = BuiltInRegistries.FLUID.byId(menu.getFluidId());
                Component fluidName = new FluidStack(fluid, 1000).getDisplayName();
                tooltip.add(Component.translatable("tooltip.water_treatment_unit.fluid",
                        fluidName, amount / 1000, capacity / 1000).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("tooltip.water_treatment_unit.empty")
                        .withStyle(ChatFormatting.GRAY));
            }
            context.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }

    private void renderPowerTooltip(GuiGraphics context, int x, int y, int mouseX, int mouseY) {
        int powerX = x + POWER_BAR_X;
        int powerY = y + POWER_BAR_Y;
        if (mouseX >= powerX && mouseX <= powerX + POWER_BAR_WIDTH
                && mouseY >= powerY && mouseY <= powerY + POWER_BAR_HEIGHT) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("tooltip.water_treatment_unit.stored_power", menu.getStoredPower())
                    .withStyle(ChatFormatting.YELLOW));
            context.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
    
    private static void applyFluidColor(Fluid fluid) {
        Fluid actual = fluid == null ? Fluids.WATER : fluid;
        int tint = IClientFluidTypeExtensions.of(actual).getTintColor();
        RenderSystem.setShaderColor(((tint >> 16) & 0xFF) / 255f, ((tint >> 8) & 0xFF) / 255f,
                (tint & 0xFF) / 255f, 0.7f);
    }
}
