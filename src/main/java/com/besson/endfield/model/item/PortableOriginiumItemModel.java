package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.PortableOriginiumRigItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PortableOriginiumItemModel extends GeoModel<PortableOriginiumRigItem> {
    @Override
    public ResourceLocation getModelResource(PortableOriginiumRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/portable_originium_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PortableOriginiumRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/portable_originium_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PortableOriginiumRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/portable_originium_rig.animation.json");
    }
}
