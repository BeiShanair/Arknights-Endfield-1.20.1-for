package com.besson.endfield.model.item.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.powering.XiraniteRelayItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class XiraniteRelayItemModel extends GeoModel<XiraniteRelayItem> {
    @Override
    public ResourceLocation getModelResource(XiraniteRelayItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/xiranite_relay.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(XiraniteRelayItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/xiranite_relay.png");
    }

    @Override
    public ResourceLocation getAnimationResource(XiraniteRelayItem animatable) {
        return null;
    }
}
