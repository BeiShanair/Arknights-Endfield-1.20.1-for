package com.besson.endfield.model.item.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.resourcing.AcidResistantPumpMkIIItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AcidResistantPumpMkIIItemModel extends GeoModel<AcidResistantPumpMkIIItem> {
    @Override
    public ResourceLocation getModelResource(AcidResistantPumpMkIIItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/acid_resistant_pump_mk_ii.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AcidResistantPumpMkIIItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/acid_resistant_pump_mk_ii.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AcidResistantPumpMkIIItem animatable) {
        return null;
    }
}
