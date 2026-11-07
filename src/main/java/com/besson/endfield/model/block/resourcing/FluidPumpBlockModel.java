package com.besson.endfield.model.block.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.resourcing.FluidPumpBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FluidPumpBlockModel extends GeoModel<FluidPumpBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FluidPumpBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/fluid_pump.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FluidPumpBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fluid_pump.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FluidPumpBlockEntity animatable) {
        return null;
    }
}
