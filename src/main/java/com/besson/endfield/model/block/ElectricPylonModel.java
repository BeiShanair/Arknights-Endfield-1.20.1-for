package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.ElectricPylonBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElectricPylonModel extends GeoModel<ElectricPylonBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ElectricPylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_pylon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricPylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_pylon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricPylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_pylon.animation.json");
    }
}
