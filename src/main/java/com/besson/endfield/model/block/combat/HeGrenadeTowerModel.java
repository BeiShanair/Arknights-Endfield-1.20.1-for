package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.HeGrenadeTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HeGrenadeTowerModel extends GeoModel<HeGrenadeTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(HeGrenadeTowerBlockEntity heGrenadeTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/he_grenade_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeGrenadeTowerBlockEntity heGrenadeTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/he_grenade_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeGrenadeTowerBlockEntity heGrenadeTowerBlockEntity) {
        return null;
    }
}
