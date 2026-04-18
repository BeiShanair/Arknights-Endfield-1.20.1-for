package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production2.PackagingUnitBlockEntity;
import com.besson.endfield.screen.custom.BaseIOScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PackagingUnitScreen extends BaseIOScreen<PackagingUnitScreenHandler, PackagingUnitBlockEntity> {

    public PackagingUnitScreen(PackagingUnitScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected ResourceLocation setTexture() {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/packaging_unit.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()) {
            context.blit(TEXTURE, x + 75, y + 40, 176, 0, menu.getScaledProgress(), 8);
        }
    }
}
