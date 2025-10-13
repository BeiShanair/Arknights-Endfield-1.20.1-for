package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.ShreddingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShreddingUnitModel extends GeoModel<ShreddingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ShreddingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/shredding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShreddingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/shredding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShreddingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/shredding_unit.animation.json");
    }
}
