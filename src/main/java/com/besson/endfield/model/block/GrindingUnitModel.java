package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.GrindingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GrindingUnitModel extends GeoModel<GrindingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GrindingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/grinding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GrindingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grinding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GrindingUnitBlockEntity animatable) {
        return null;
    }
}
