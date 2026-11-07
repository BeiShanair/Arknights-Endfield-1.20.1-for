package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.SurgeTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.SurgeTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class SurgeTowerModel extends GeoModel<SurgeTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SurgeTowerBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/surge_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SurgeTowerBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof SurgeTowerBlock b) {
            if (b.defaultBlockState().getValue(SurgeTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/surge_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/surge_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SurgeTowerBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(SurgeTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
