package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.FittingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FittingUnitModel extends GeoModel<FittingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FittingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/fitting_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FittingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fitting_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FittingUnitBlockEntity animatable) {
        return null;
    }
}
