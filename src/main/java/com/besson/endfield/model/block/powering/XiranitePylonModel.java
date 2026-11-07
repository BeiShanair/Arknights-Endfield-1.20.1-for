package com.besson.endfield.model.block.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.powering.XiranitePylonBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class XiranitePylonModel extends GeoModel<XiranitePylonBlockEntity> {
    @Override
    public ResourceLocation getModelResource(XiranitePylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/xiranite_pylon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(XiranitePylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/xiranite_pylon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(XiranitePylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/xiranite_pylon.animation.json");
    }
}
