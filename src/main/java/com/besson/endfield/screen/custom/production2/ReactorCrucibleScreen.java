package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.ArknightsEndfield;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ReactorCrucibleScreen extends BaseCrucibleScreen<ReactorCrucibleScreenHandler> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/reactor_crucible.png");

    private static final int[] INPUT_X = {79, 47, 111, 60, 98};
    private static final int[] INPUT_Y = {31, 55, 55, 90, 90};

    public ReactorCrucibleScreen(ReactorCrucibleScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected int[] getInputX() {
        return INPUT_X;
    }

    @Override
    protected int[] getInputY() {
        return INPUT_Y;
    }
}
