package com.besson.endfield.model.block.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.resourcing.AcidResistantPumpMkIIBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AcidResistantPumpMkIIBlockModel extends GeoModel<AcidResistantPumpMkIIBlockEntity> {
    @Override
    public ResourceLocation getModelResource(AcidResistantPumpMkIIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/acid_resistant_pump_mk_ii.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AcidResistantPumpMkIIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/acid_resistant_pump_mk_ii.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AcidResistantPumpMkIIBlockEntity animatable) {
        return null;
    }
}
