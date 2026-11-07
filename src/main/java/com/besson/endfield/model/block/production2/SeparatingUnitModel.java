package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production2.SeparatingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SeparatingUnitModel extends GeoModel<SeparatingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SeparatingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/separating_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeparatingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/separating_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeparatingUnitBlockEntity animatable) {
        return null;
    }
}
