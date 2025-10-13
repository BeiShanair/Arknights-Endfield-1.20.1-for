package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.SeedPickingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SeedPickingUnitModel extends GeoModel<SeedPickingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SeedPickingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/seed_picking_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeedPickingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/seed_picking_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeedPickingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/seed_picking_unit.animation.json");
    }
}
