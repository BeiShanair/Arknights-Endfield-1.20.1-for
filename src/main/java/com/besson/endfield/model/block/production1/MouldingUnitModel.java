package com.besson.endfield.model.block.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production1.MouldingUnitBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MouldingUnitModel extends GeoModel<MouldingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(MouldingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/moulding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MouldingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/moulding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MouldingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/moulding_unit.animation.json");
    }
}
