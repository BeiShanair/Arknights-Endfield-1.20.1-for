package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.OmnidirectionalSonicTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OmnidirectionalSonicTowerModel extends GeoModel<OmnidirectionalSonicTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(OmnidirectionalSonicTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/omnidirectional_sonic_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OmnidirectionalSonicTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/omnidirectional_sonic_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OmnidirectionalSonicTowerBlockEntity animatable) {
        return null;
    }
}
