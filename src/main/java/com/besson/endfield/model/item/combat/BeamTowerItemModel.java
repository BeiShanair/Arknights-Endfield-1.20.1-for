package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.BeamTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BeamTowerItemModel extends GeoModel<BeamTowerItem> {
    @Override
    public ResourceLocation getModelResource(BeamTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/beam_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BeamTowerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/beam_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BeamTowerItem animatable) {
        return null;
    }
}
