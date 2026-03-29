package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.GrenadeTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GrenadeTowerModel extends GeoModel<GrenadeTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GrenadeTowerBlockEntity grenadeTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/grenade_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GrenadeTowerBlockEntity grenadeTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grenade_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GrenadeTowerBlockEntity grenadeTowerBlockEntity) {
        return null;
    }
}
