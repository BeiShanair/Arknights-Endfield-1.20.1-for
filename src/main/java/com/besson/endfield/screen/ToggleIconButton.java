package com.besson.endfield.screen;

import com.besson.endfield.ArknightsEndfield;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ToggleIconButton extends AbstractButton {
    private static final ResourceLocation SWITCH_ENABLE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/button/switch_enable.png");
    private static final ResourceLocation SWITCH_DISABLE = ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/button/switch_disable.png");

    private final BooleanSupplier booleanSupplier;
    private final Consumer<Boolean> onToggle;

    public ToggleIconButton(int i, int j, BooleanSupplier booleanSupplier, Consumer<Boolean> onToggle) {
        super(i, j, 16, 16, Component.empty());
        this.booleanSupplier = booleanSupplier;
        this.onToggle = onToggle;
    }

    @Override
    public void onPress() {
        boolean newState = !booleanSupplier.getAsBoolean();
        onToggle.accept(newState);
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        ResourceLocation texture = booleanSupplier.getAsBoolean() ? SWITCH_ENABLE : SWITCH_DISABLE;
        context.blit(texture, getX(), getY(), 0, 0, this.width, this.height, 16, 16);

        if (isHovered()) {
            context.fill(getX(), getY(), getX() + this.width, getY() + this.height, 0x40FFFFFF);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        
    }
}
