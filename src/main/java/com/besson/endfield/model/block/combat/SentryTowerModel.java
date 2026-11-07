package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.SentryTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.SentryTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class SentryTowerModel extends GeoModel<SentryTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SentryTowerBlockEntity sentryTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/sentry_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SentryTowerBlockEntity sentryTowerBlockEntity) {
        Block block = sentryTowerBlockEntity.getBlockState().getBlock();
        if (block instanceof SentryTowerBlock b) {
            if (b.defaultBlockState().getValue(SentryTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/sentry_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/sentry_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SentryTowerBlockEntity sentryTowerBlockEntity) {
        return null;
    }

    @Override
    public RenderType getRenderType(SentryTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
