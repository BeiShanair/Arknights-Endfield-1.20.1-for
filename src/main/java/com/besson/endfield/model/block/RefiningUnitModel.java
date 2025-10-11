package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.RefiningUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RefiningUnitModel extends GeoModel<RefiningUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(RefiningUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/refining_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RefiningUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/refining_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RefiningUnitBlockEntity animatable) {
        return null;
    }
}
