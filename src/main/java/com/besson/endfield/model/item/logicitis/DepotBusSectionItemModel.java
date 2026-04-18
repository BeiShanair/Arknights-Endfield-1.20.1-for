package com.besson.endfield.model.item.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.logicitis.DepotBusSectionItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DepotBusSectionItemModel extends GeoModel<DepotBusSectionItem> {
    @Override
    public ResourceLocation getModelResource(DepotBusSectionItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_bus_section.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotBusSectionItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_bus_section.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotBusSectionItem animatable) {
        return null;
    }
}
