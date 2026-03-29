package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.HeavyGunTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HeavyGunTowerItemModel extends GeoModel<HeavyGunTowerItem> {
    @Override
    public ResourceLocation getModelResource(HeavyGunTowerItem animatable) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "geo/heavy_gun_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeavyGunTowerItem animatable) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "textures/block/heavy_gun_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeavyGunTowerItem animatable) {
        return null;
    }
}
