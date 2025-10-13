package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.FillingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FillingUnitItemModel extends GeoModel<FillingUnitItem> {
    @Override
    public ResourceLocation getModelResource(FillingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/filling_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FillingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/filling_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FillingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/filling_unit.animation.json");
    }
}
