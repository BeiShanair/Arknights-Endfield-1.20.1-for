package com.besson.endfield.model.item.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.logicitis.ProtocolStashItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ProtocolStashItemModel extends GeoModel<ProtocolStashItem> {
    @Override
    public ResourceLocation getModelResource(ProtocolStashItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/protocol_stash.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProtocolStashItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/protocol_stash.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ProtocolStashItem animatable) {
        return null;
    }
}
