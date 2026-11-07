package com.besson.endfield.model.item.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.production1.WaterTreatmentUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WaterTreatmentUnitItemModel extends GeoModel<WaterTreatmentUnitItem> {
    @Override
    public ResourceLocation getModelResource(WaterTreatmentUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/water_treatment_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WaterTreatmentUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/water_treatment_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WaterTreatmentUnitItem animatable) {
        return null;
    }
}
