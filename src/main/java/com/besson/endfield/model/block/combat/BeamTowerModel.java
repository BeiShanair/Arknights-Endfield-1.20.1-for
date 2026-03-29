package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.BeamTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BeamTowerModel extends GeoModel<BeamTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(BeamTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/beam_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BeamTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/beam_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BeamTowerBlockEntity animatable) {
        return null;
    }
}
