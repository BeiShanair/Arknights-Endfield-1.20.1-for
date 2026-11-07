package com.besson.endfield.model.block.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.powering.RelayTowerBlock;
import com.besson.endfield.blockEntity.custom.powering.RelayTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class RelayTowerModel extends GeoModel<RelayTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(RelayTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/relay_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RelayTowerBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof RelayTowerBlock b) {
            if (b.defaultBlockState().getValue(RelayTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/relay_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/relay_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RelayTowerBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(RelayTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
