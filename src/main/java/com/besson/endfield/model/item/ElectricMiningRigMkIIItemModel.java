package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.ElectricMiningRigMkIIItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElectricMiningRigMkIIItemModel extends GeoModel<ElectricMiningRigMkIIItem> {
    @Override
    public ResourceLocation getModelResource(ElectricMiningRigMkIIItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_mining_rig_mk_ii.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricMiningRigMkIIItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig_mk_ii.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricMiningRigMkIIItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_mining_rig_mk_ii.animation.json");
    }
}
