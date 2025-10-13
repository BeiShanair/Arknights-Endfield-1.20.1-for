package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.PackagingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PackagingUnitItemModel extends GeoModel<PackagingUnitItem> {
    @Override
    public ResourceLocation getModelResource(PackagingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/packaging_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PackagingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/packaging_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PackagingUnitItem animatable) {
        return null;
    }
}
