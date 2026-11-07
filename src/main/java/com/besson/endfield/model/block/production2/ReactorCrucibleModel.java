package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production2.ReactorCrucibleBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ReactorCrucibleModel extends GeoModel<ReactorCrucibleBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ReactorCrucibleBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/reactor_crucible.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ReactorCrucibleBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/reactor_crucible.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ReactorCrucibleBlockEntity animatable) {
        return null;
    }
}
