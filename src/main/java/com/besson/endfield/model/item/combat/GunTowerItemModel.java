package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.GunTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GunTowerItemModel extends GeoModel<GunTowerItem> {
    @Override
    public ResourceLocation getModelResource(GunTowerItem gunTowerItem) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/gun_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GunTowerItem gunTowerItem) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gun_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GunTowerItem gunTowerItem) {
        return null;
    }
}
