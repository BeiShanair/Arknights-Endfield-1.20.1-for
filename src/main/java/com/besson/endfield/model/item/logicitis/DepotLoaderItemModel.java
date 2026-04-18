package com.besson.endfield.model.item.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.logicitis.DepotLoaderItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DepotLoaderItemModel extends GeoModel<DepotLoaderItem> {
    @Override
    public ResourceLocation getModelResource(DepotLoaderItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_loader.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotLoaderItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_loader.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotLoaderItem animatable) {
        return null;
    }
}
