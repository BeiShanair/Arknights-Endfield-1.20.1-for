package com.besson.endfield.model.item.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.resourcing.FluidPumpItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FluidPumpItemModel extends GeoModel<FluidPumpItem> {
    @Override
    public ResourceLocation getModelResource(FluidPumpItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/fluid_pump.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FluidPumpItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fluid_pump.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FluidPumpItem animatable) {
        return null;
    }
}
