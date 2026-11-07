package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.OmnidirectionalSonicTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.OmnidirectionalSonicTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class OmnidirectionalSonicTowerModel extends GeoModel<OmnidirectionalSonicTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(OmnidirectionalSonicTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/omnidirectional_sonic_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OmnidirectionalSonicTowerBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof OmnidirectionalSonicTowerBlock b) {
            if (b.defaultBlockState().getValue(OmnidirectionalSonicTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/omnidirectional_sonic_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/omnidirectional_sonic_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OmnidirectionalSonicTowerBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(OmnidirectionalSonicTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
