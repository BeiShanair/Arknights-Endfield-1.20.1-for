package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.PackagingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PackagingUnitModel extends GeoModel<PackagingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(PackagingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/packaging_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PackagingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/packaging_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PackagingUnitBlockEntity animatable) {
        return null;
    }
}
