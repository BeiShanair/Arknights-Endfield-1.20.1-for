package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.depot.FluidTankBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FluidTankModel extends GeoModel<FluidTankBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FluidTankBlockEntity fluidTankBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/fluid_tank.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FluidTankBlockEntity fluidTankBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fluid_tank.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FluidTankBlockEntity fluidTankBlockEntity) {
        return null;
    }
}
