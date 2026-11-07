package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.BeamTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.BeamTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class BeamTowerModel extends GeoModel<BeamTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(BeamTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/beam_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BeamTowerBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof BeamTowerBlock b) {
            if (b.defaultBlockState().getValue(BeamTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/beam_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/beam_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BeamTowerBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(BeamTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
