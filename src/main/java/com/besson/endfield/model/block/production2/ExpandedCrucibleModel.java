package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production2.ExpandedCrucibleBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ExpandedCrucibleModel extends GeoModel<ExpandedCrucibleBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ExpandedCrucibleBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/expanded_crucible.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ExpandedCrucibleBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/expanded_crucible.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ExpandedCrucibleBlockEntity animatable) {
        return null;
    }
}
