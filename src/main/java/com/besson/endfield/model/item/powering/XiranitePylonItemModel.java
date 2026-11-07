package com.besson.endfield.model.item.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.powering.XiranitePylonItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class XiranitePylonItemModel extends GeoModel<XiranitePylonItem> {
    @Override
    public ResourceLocation getModelResource(XiranitePylonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/xiranite_pylon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(XiranitePylonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/xiranite_pylon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(XiranitePylonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/xiranite_pylon.animation.json");
    }
}
