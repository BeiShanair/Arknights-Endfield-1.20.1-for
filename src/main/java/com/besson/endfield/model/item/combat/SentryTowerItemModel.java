package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.SentryTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SentryTowerItemModel extends GeoModel<SentryTowerItem> {
    @Override
    public ResourceLocation getModelResource(SentryTowerItem sentryTowerItem) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "geo/sentry_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SentryTowerItem sentryTowerItem) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "textures/block/sentry_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SentryTowerItem sentryTowerItem) {
        return null;
    }
}
