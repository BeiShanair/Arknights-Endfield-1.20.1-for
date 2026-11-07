package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.MedicalTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.MedicalTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class MedicalTowerModel extends GeoModel<MedicalTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(MedicalTowerBlockEntity medicalTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/medical_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MedicalTowerBlockEntity medicalTowerBlockEntity) {
        Block block = medicalTowerBlockEntity.getBlockState().getBlock();
        if (block instanceof MedicalTowerBlock b) {
            if (b.defaultBlockState().getValue(MedicalTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/medical_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/medical_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MedicalTowerBlockEntity medicalTowerBlockEntity) {
        return null;
    }

    @Override
    public RenderType getRenderType(MedicalTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
