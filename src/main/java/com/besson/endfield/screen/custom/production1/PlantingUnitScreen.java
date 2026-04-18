package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production1.PlantingUnitBlockEntity;
import com.besson.endfield.screen.custom.BaseIOScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PlantingUnitScreen extends BaseIOScreen<PlantingUnitScreenHandler, PlantingUnitBlockEntity> {
    public PlantingUnitScreen(PlantingUnitScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected ResourceLocation setTexture() {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/planting_unit.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()){
            context.blit(TEXTURE,x + 85, y + 30, 176,0,8, menu.getScaledProgress());
        }
    }
}
