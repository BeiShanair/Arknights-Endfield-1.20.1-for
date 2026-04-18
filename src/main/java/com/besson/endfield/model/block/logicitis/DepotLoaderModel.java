package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.logicitis.DepotLoaderBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DepotLoaderModel extends GeoModel<DepotLoaderBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DepotLoaderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_loader.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotLoaderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_loader.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotLoaderBlockEntity animatable) {
        return null;
    }
}
