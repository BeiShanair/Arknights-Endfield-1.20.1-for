package com.besson.endfield.model.item.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.production2.ExpandedCrucibleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ExpandedCrucibleItemModel extends GeoModel<ExpandedCrucibleItem> {
    @Override
    public ResourceLocation getModelResource(ExpandedCrucibleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/expanded_crucible.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ExpandedCrucibleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/expanded_crucible.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ExpandedCrucibleItem animatable) {
        return null;
    }
}
