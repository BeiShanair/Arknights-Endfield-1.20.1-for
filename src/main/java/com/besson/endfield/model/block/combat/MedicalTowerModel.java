package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.MedicalTowerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MedicalTowerModel extends GeoModel<MedicalTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(MedicalTowerBlockEntity medicalTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/medical_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MedicalTowerBlockEntity medicalTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/medical_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MedicalTowerBlockEntity medicalTowerBlockEntity) {
        return null;
    }
}
