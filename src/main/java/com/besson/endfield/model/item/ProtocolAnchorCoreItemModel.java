package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.ProtocolAnchorCoreItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ProtocolAnchorCoreItemModel extends GeoModel<ProtocolAnchorCoreItem> {
    @Override
    public ResourceLocation getModelResource(ProtocolAnchorCoreItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/protocol_anchor_core.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProtocolAnchorCoreItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/protocol_anchor_core.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ProtocolAnchorCoreItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/protocol_anchor_core.animation.json");
    }
}
