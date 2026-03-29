package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.SentryTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SentryTowerModel extends GeoModel<SentryTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SentryTowerBlockEntity sentryTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/sentry_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SentryTowerBlockEntity sentryTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/sentry_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SentryTowerBlockEntity sentryTowerBlockEntity) {
        return null;
    }
}
