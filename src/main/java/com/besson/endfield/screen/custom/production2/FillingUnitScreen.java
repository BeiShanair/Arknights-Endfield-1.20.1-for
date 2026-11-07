package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production2.FillingUnitBlockEntity;
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

public class FillingUnitScreen extends BaseIOScreen<FillingUnitScreenHandler, FillingUnitBlockEntity> {
    private static final ResourceLocation WATER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/water_still.png");
    private static final int FLUID_SLOT_X = 47;
    private static final int FLUID_SLOT_Y = 49;
    private static final int SLOT_SIZE = 16;

    private static final int POWER_BAR_X = 158;
    private static final int POWER_BAR_Y = 11;
    private static final int POWER_BAR_WIDTH = 8;
    private static final int POWER_BAR_HEIGHT = 64;

    public FillingUnitScreen(FillingUnitScreenHandler handler, Inventory inventory, Component title) {
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
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/221.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(TEXTURE, x + 75, y + 40, 176, 0, menu.getScaledProgress(), 8);
        }
        renderStoredPowerBar(context, x, y);
        renderSlotFluid(context, x, y);
    }

    private void renderStoredPowerBar(GuiGraphics context, int x, int y) {
        int storedPower = menu.getStoredPower();
        int powerHeight = (int) ((10000F - storedPower) / 10000f * POWER_BAR_HEIGHT);
        if (powerHeight <= 0) return;
        context.blit(TEXTURE, x + POWER_BAR_X, y + POWER_BAR_Y, 204, 0, POWER_BAR_WIDTH, powerHeight);
    }
    
    private void renderSlotFluid(GuiGraphics context, int x, int y) {
        if (!menu.nowFluidMode()) return;

        int capacity = menu.getFluidCapacity();
        int amount = menu.getFluidAmount();
        if (capacity <= 0 || amount <= 0) return;

        int fluidX = x + FLUID_SLOT_X;
        int fluidY = y + FLUID_SLOT_Y;
        int fluidHeight = Math.max(1, (int) (amount / (float) capacity * SLOT_SIZE));
        if (fluidHeight > SLOT_SIZE) fluidHeight = SLOT_SIZE;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        applyFluidColor(BuiltInRegistries.FLUID.byId(menu.getFluidId()));
        
        int drawY = fluidY + SLOT_SIZE - fluidHeight;
        for (int yOffset = 0; yOffset < fluidHeight; yOffset += 16) {
            int drawHeight = Math.min(16, fluidHeight - yOffset);
            context.blit(WATER_TEXTURE, fluidX, drawY + yOffset, 0, 0, SLOT_SIZE, drawHeight, 16, 16);
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

        int fluidX = bgX + FLUID_SLOT_X;
        int fluidY = bgY + FLUID_SLOT_Y;
        if (mouseX >= fluidX && mouseX <= fluidX + SLOT_SIZE &&
                mouseY >= fluidY && mouseY <= fluidY + SLOT_SIZE) {
            int amount = menu.getFluidAmount();
            int capacity = menu.getFluidCapacity();

            List<Component> tooltip = new ArrayList<>();
            if (amount > 0) {
                tooltip.add(Component.translatable("tooltip.filling_unit.tank",
                        amount / 1000, capacity / 1000).withStyle(ChatFormatting.AQUA));
            } else {
                tooltip.add(Component.translatable("tooltip.filling_unit.tank_empty")
                        .withStyle(ChatFormatting.GRAY));
            }
            context.renderTooltip(this.font, tooltip, Optional.empty(), mouseX, mouseY);
        }
    }
}
