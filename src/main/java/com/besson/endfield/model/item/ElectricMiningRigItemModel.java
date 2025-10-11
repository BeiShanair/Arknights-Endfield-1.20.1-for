package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.ElectricMiningRigItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElectricMiningRigItemModel extends GeoModel<ElectricMiningRigItem> {
    @Override
    public ResourceLocation getModelResource(ElectricMiningRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_mining_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricMiningRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricMiningRigItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_mining_rig.animation.json");
    }
}
