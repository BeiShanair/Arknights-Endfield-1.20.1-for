package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.OmnidirectionalSonicTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OmnidirectionalSonicTowerItemModel extends GeoModel<OmnidirectionalSonicTowerItem> {
    @Override
    public ResourceLocation getModelResource(OmnidirectionalSonicTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/omnidirectional_sonic_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OmnidirectionalSonicTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/omnidirectional_sonic_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OmnidirectionalSonicTowerItem animatable) {
        return null;
    }
}
