package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.SeedPickingUnitItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SeedPickingUnitItemModel extends GeoModel<SeedPickingUnitItem> {
    @Override
    public ResourceLocation getModelResource(SeedPickingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/seed_picking_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeedPickingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/seed_picking_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeedPickingUnitItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/seed_picking_unit.animation.json");
    }
}
