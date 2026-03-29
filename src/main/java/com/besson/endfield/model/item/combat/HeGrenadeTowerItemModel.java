package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.HeGrenadeTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HeGrenadeTowerItemModel extends GeoModel<HeGrenadeTowerItem> {
    @Override
    public ResourceLocation getModelResource(HeGrenadeTowerItem heGrenadeTowerItem) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "geo/he_grenade_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeGrenadeTowerItem heGrenadeTowerItem) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "textures/block/he_grenade_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeGrenadeTowerItem heGrenadeTowerItem) {
        return null;
    }
}
