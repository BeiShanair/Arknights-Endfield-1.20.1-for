package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.LNTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.LNTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class LNTowerModel extends GeoModel<LNTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(LNTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/ln_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LNTowerBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof LNTowerBlock b) {
            if (b.defaultBlockState().getValue(LNTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/ln_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/ln_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LNTowerBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(LNTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
