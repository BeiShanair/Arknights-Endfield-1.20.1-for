package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.ElectricPylonItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElectricPylonItemModel extends GeoModel<ElectricPylonItem> {
    @Override
    public ResourceLocation getModelResource(ElectricPylonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_pylon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricPylonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_pylon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricPylonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_pylon.animation.json");
    }
}
