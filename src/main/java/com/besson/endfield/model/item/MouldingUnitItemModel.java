package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.MouldingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MouldingUnitItemModel extends GeoModel<MouldingUnitItem> {
    @Override
    public ResourceLocation getModelResource(MouldingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/moulding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MouldingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/moulding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MouldingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/moulding_unit.animation.json");
    }
}
