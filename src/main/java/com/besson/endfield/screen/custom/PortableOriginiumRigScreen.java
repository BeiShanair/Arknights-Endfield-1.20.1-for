package com.besson.endfield.screen.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.resourcing.PortableOriginiumRigBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PortableOriginiumRigScreen extends BaseRigScreen<PortableOriginiumRigScreenHandler, PortableOriginiumRigBlockEntity> {
    public PortableOriginiumRigScreen(PortableOriginiumRigScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected ResourceLocation setTexture() {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/gui/portable_originium_rig.png");
    }

    @Override
    protected void renderProgressArrow(GuiGraphics context, int x, int y) {
        if (menu.isCrafting()){
            context.blit(TEXTURE,x + 68, y + 41, 176,0, menu.getScaledProgress(), 8);
        }
    }
}
