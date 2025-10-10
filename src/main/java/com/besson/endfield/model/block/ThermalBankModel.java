package com.besson.endfield.model.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.ThermalBankBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ThermalBankModel extends GeoModel<ThermalBankBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ThermalBankBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/thermal_bank.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThermalBankBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/thermal_bank.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThermalBankBlockEntity animatable) {
        return null;
    }
}
