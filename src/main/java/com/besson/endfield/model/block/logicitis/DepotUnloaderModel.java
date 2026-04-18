package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.logicitis.DepotUnloaderBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DepotUnloaderModel extends GeoModel<DepotUnloaderBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DepotUnloaderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_unloader.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotUnloaderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_unloader.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotUnloaderBlockEntity animatable) {
        return null;
    }
}
