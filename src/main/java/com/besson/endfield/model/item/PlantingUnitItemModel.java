package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.PlantingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PlantingUnitItemModel extends GeoModel<PlantingUnitItem> {
    @Override
    public ResourceLocation getModelResource(PlantingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/planting_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlantingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/planting_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PlantingUnitItem animatable) {
        return null;
    }
}
