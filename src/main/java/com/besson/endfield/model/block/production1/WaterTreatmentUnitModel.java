package com.besson.endfield.model.block.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production1.WaterTreatmentUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WaterTreatmentUnitModel extends GeoModel<WaterTreatmentUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(WaterTreatmentUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/water_treatment_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WaterTreatmentUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/water_treatment_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WaterTreatmentUnitBlockEntity animatable) {
        return null;
    }
}
