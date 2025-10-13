package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.GearingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GearingUnitItemModel extends GeoModel<GearingUnitItem> {
    @Override
    public ResourceLocation getModelResource(GearingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/gearing_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GearingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gearing_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GearingUnitItem animatable) {
        return null;
    }
}
