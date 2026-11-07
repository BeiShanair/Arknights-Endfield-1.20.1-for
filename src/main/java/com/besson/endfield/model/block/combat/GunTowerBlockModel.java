package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.GunTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.GunTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class GunTowerBlockModel extends GeoModel<GunTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GunTowerBlockEntity gunTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/gun_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GunTowerBlockEntity gunTowerBlockEntity) {
        Block block = gunTowerBlockEntity.getBlockState().getBlock();
        if (block instanceof GunTowerBlock b) {
            if (b.defaultBlockState().getValue(GunTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gun_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gun_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GunTowerBlockEntity gunTowerBlockEntity) {
        return null;
    }

    @Override
    public RenderType getRenderType(GunTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
