package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.HeGrenadeTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.HeGrenadeTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class HeGrenadeTowerModel extends GeoModel<HeGrenadeTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(HeGrenadeTowerBlockEntity heGrenadeTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/he_grenade_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeGrenadeTowerBlockEntity heGrenadeTowerBlockEntity) {
        Block block = heGrenadeTowerBlockEntity.getBlockState().getBlock();
        if (block instanceof HeGrenadeTowerBlock b) {
            if (b.defaultBlockState().getValue(HeGrenadeTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/he_grenade_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/he_grenade_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeGrenadeTowerBlockEntity heGrenadeTowerBlockEntity) {
        return null;
    }

    @Override
    public RenderType getRenderType(HeGrenadeTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
