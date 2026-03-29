package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.HeavyGunTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HeavyGunTowerModel extends GeoModel<HeavyGunTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(HeavyGunTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/heavy_gun_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeavyGunTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/heavy_gun_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeavyGunTowerBlockEntity animatable) {
        return null;
    }
}
