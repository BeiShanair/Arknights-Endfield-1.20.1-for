package com.besson.endfield.model.block.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.resourcing.HydroMiningRigBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HydroMiningRigModel extends GeoModel<HydroMiningRigBlockEntity> {
    @Override
    public ResourceLocation getModelResource(HydroMiningRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/hydro_mining_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HydroMiningRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/hydro_mining_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HydroMiningRigBlockEntity animatable) {
        return null;
    }
}
