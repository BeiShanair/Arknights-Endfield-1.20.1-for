package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.LNTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LNTowerModel extends GeoModel<LNTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(LNTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/ln_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LNTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/ln_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LNTowerBlockEntity animatable) {
        return null;
    }
}
