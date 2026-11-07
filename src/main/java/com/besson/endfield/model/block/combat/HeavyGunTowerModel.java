package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.HeavyGunTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.HeavyGunTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class HeavyGunTowerModel extends GeoModel<HeavyGunTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(HeavyGunTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/heavy_gun_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeavyGunTowerBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof HeavyGunTowerBlock b) {
            if (b.defaultBlockState().getValue(HeavyGunTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/heavy_gun_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/heavy_gun_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeavyGunTowerBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(HeavyGunTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
