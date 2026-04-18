package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production1.FittingUnitBlockEntity;
import com.besson.endfield.screen.custom.BaseIOScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FittingUnitScreen extends BaseIOScreen<FittingUnitScreenHandler, FittingUnitBlockEntity> {
    public FittingUnitScreen(FittingUnitScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected ResourceLocation setTexture() {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/fitting_unit.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()){
            context.blit(TEXTURE,x + 85, y + 30, 176,0,8,menu.getScaledProgress());
        }
    }
}
