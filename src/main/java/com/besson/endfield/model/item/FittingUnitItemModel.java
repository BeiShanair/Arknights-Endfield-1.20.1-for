package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.FittingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FittingUnitItemModel extends GeoModel<FittingUnitItem> {
    @Override
    public ResourceLocation getModelResource(FittingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/fitting_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FittingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fitting_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FittingUnitItem animatable) {
        return null;
    }
}
