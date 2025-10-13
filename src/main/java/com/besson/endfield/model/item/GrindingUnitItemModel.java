package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.GrindingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GrindingUnitItemModel extends GeoModel<GrindingUnitItem> {
    @Override
    public ResourceLocation getModelResource(GrindingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/grinding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GrindingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grinding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GrindingUnitItem animatable) {
        return null;
    }
}
