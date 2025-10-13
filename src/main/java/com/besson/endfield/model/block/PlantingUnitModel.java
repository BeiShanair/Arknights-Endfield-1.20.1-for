package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.PlantingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PlantingUnitModel extends GeoModel<PlantingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(PlantingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/planting_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlantingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/planting_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PlantingUnitBlockEntity animatable) {
        return null;
    }
}
