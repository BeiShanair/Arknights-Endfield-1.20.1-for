package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.PortableOriginiumRigBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PortableOriginiumModel extends GeoModel<PortableOriginiumRigBlockEntity> {
    @Override
    public ResourceLocation getModelResource(PortableOriginiumRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/portable_originium_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PortableOriginiumRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/portable_originium_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PortableOriginiumRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/portable_originium_rig.animation.json");
    }
}
