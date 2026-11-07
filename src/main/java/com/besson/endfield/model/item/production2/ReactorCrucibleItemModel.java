package com.besson.endfield.model.item.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.production2.ReactorCrucibleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ReactorCrucibleItemModel extends GeoModel<ReactorCrucibleItem> {
    @Override
    public ResourceLocation getModelResource(ReactorCrucibleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/reactor_crucible.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ReactorCrucibleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/reactor_crucible.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ReactorCrucibleItem animatable) {
        return null;
    }
}
