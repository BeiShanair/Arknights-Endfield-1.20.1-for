package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.GrenadeTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GrenadeTowerItemModel extends GeoModel<GrenadeTowerItem> {
    @Override
    public ResourceLocation getModelResource(GrenadeTowerItem grenadeTowerItem) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/grenade_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GrenadeTowerItem grenadeTowerItem) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grenade_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GrenadeTowerItem grenadeTowerItem) {
        return null;
    }
}
