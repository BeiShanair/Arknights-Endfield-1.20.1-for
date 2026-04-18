package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.SurgeTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SurgeTowerItemModel extends GeoModel<SurgeTowerItem> {
    @Override
    public ResourceLocation getModelResource(SurgeTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/surge_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SurgeTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/surge_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SurgeTowerItem animatable) {
        return null;
    }
}
