package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.ElectricMiningRigMkIIBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElectricMiningRigMkIIModel extends GeoModel<ElectricMiningRigMkIIBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ElectricMiningRigMkIIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_mining_rig_mk_ii.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricMiningRigMkIIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig_mk_ii.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricMiningRigMkIIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_mining_rig_mk_ii.animation.json");
    }
}
