package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.RelayTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RelayTowerModel extends GeoModel<RelayTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(RelayTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/relay_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RelayTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/relay_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RelayTowerBlockEntity animatable) {
        return null;
    }
}
