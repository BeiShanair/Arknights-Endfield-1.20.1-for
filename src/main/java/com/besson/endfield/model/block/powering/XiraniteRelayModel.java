package com.besson.endfield.model.block.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.powering.XiraniteRelayBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class XiraniteRelayModel extends GeoModel<XiraniteRelayBlockEntity> {
    @Override
    public ResourceLocation getModelResource(XiraniteRelayBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/xiranite_relay.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(XiraniteRelayBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/xiranite_relay.png");
    }

    @Override
    public ResourceLocation getAnimationResource(XiraniteRelayBlockEntity animatable) {
        return null;
    }
}
