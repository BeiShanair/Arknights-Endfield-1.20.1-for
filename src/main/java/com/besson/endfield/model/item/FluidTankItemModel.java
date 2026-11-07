package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.FluidTankItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FluidTankItemModel extends GeoModel<FluidTankItem> {
    @Override
    public ResourceLocation getModelResource(FluidTankItem fluidTankItem) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/fluid_tank.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FluidTankItem fluidTankItem) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fluid_tank.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FluidTankItem fluidTankItem) {
        return null;
    }
}
