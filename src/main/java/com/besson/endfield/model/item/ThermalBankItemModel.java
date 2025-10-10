package com.besson.endfield.model.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.ThermalBankItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ThermalBankItemModel extends GeoModel<ThermalBankItem> {
    @Override
    public ResourceLocation getModelResource(ThermalBankItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/thermal_bank.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThermalBankItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/thermal_bank.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThermalBankItem animatable) {
        return null;
    }
}
