package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.RefiningUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RefiningUnitItemModel extends GeoModel<RefiningUnitItem> {
    @Override
    public ResourceLocation getModelResource(RefiningUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/refining_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RefiningUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/refining_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RefiningUnitItem animatable) {
        return null;
    }
}
