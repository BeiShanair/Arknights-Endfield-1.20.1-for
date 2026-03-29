package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.SurgeTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SurgeTowerModel extends GeoModel<SurgeTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SurgeTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/surge_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SurgeTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/surge_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SurgeTowerBlockEntity animatable) {
        return null;
    }
}
