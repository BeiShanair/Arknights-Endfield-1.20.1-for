package com.besson.endfield.model.item.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.production2.SeparatingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SeparatingUnitItemModel extends GeoModel<SeparatingUnitItem> {
    @Override
    public ResourceLocation getModelResource(SeparatingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/separating_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeparatingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/separating_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeparatingUnitItem animatable) {
        return null;
    }
}
