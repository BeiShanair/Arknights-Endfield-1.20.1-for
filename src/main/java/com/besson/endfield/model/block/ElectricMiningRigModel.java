package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.ElectricMiningRigBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElectricMiningRigModel extends GeoModel<ElectricMiningRigBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ElectricMiningRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_mining_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricMiningRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricMiningRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_mining_rig.animation.json");
    }
}
