package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.FillingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FillingUnitModel extends GeoModel<FillingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FillingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/filling_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FillingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/filling_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FillingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/filling_unit.animation.json");
    }
}
