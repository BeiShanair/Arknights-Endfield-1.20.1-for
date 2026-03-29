package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.MedicalTowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MedicalTowerItemModel extends GeoModel<MedicalTowerItem> {
    @Override
    public ResourceLocation getModelResource(MedicalTowerItem medicalTowerItem) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "geo/medical_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MedicalTowerItem medicalTowerItem) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "textures/block/medical_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MedicalTowerItem medicalTowerItem) {
        return null;
    }
}
