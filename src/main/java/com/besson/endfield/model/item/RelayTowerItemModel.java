package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.RelayTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RelayTowerItemModel extends GeoModel<RelayTowerItem> {
    @Override
    public ResourceLocation getModelResource(RelayTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/relay_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RelayTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/relay_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RelayTowerItem animatable) {
        return null;
    }
}
