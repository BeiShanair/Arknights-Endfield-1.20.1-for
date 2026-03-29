package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.GunTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GunTowerBlockModel extends GeoModel<GunTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GunTowerBlockEntity gunTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/gun_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GunTowerBlockEntity gunTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gun_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GunTowerBlockEntity gunTowerBlockEntity) {
        return null;
    }
}
