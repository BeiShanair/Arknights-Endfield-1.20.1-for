package com.besson.endfield.screen;

import com.besson.endfield.ArknightsEndfield;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class FluidModeButton extends AbstractButton {
    private static final ResourceLocation SWITCH_ENABLE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/button/fluid_open.png");
    private static final ResourceLocation SWITCH_DISABLE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/button/fluid_close.png");

    private final Font font;
    private final BooleanSupplier booleanSupplier;
    private final Consumer<Boolean> onToggle;

    public FluidModeButton(Font font, int i, int j, BooleanSupplier booleanSupplier, Consumer<Boolean> onToggle) {
        super(i, j, 16, 16, Component.empty());
        this.font = font;
        this.booleanSupplier = booleanSupplier;
        this.onToggle = onToggle;
    }

    @Override
    public void onPress() {
        boolean newState = !booleanSupplier.getAsBoolean();
        onToggle.accept(newState);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        boolean open = booleanSupplier.getAsBoolean();
        graphics.blit(open ? SWITCH_ENABLE : SWITCH_DISABLE, getX(), getY(), 0, 0, this.width, this.height, 16, 16);

        if (isHovered()) {
            graphics.fill(getX(), getY(), getX() + this.width, getY() + this.height, 0x40FFFFFF);
            graphics.renderTooltip(font,
                    Component.translatable(open ? "endfield.fluid_mode.open" : "endfield.fluid_mode.close"),
                    mouseX, mouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
