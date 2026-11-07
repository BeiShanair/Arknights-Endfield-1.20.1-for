package com.besson.endfield.model.item.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.resourcing.HydroMiningRigItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HydroMiningRigItemModel extends GeoModel<HydroMiningRigItem> {
    @Override
    public ResourceLocation getModelResource(HydroMiningRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/hydro_mining_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HydroMiningRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/hydro_mining_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HydroMiningRigItem animatable) {
        return null;
    }
}
