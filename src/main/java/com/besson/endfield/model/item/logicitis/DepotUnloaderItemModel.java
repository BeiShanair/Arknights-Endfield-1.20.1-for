package com.besson.endfield.model.item.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.logicitis.DepotUnloaderItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DepotUnloaderItemModel extends GeoModel<DepotUnloaderItem> {
    @Override
    public ResourceLocation getModelResource(DepotUnloaderItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_unloader.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotUnloaderItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_unloader.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotUnloaderItem animatable) {
        return null;
    }
}
