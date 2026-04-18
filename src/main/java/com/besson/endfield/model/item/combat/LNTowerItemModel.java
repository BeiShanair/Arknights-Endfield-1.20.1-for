package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.LNTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LNTowerItemModel extends GeoModel<LNTowerItem> {
    @Override
    public ResourceLocation getModelResource(LNTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/ln_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LNTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/ln_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LNTowerItem animatable) {
        return null;
    }
}
