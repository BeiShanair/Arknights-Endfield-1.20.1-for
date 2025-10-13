package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.GearingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GearingUnitModel extends GeoModel<GearingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GearingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/gearing_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GearingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gearing_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GearingUnitBlockEntity animatable) {
        return null;
    }
}
