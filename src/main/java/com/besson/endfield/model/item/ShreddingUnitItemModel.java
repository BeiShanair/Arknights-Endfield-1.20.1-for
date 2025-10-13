package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.ShreddingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShreddingUnitItemModel extends GeoModel<ShreddingUnitItem> {
    @Override
    public ResourceLocation getModelResource(ShreddingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/shredding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShreddingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/shredding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShreddingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/shredding_unit.animation.json");
    }
}
