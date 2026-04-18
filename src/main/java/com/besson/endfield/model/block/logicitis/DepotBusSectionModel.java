package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.logicitis.DepotBusSectionBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DepotBusSectionModel extends GeoModel<DepotBusSectionBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DepotBusSectionBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_bus_section.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotBusSectionBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_bus_section.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotBusSectionBlockEntity animatable) {
        return null;
    }
}
