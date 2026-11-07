package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.GrenadeTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.GrenadeTowerBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class GrenadeTowerModel extends GeoModel<GrenadeTowerBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GrenadeTowerBlockEntity grenadeTowerBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/grenade_tower.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GrenadeTowerBlockEntity grenadeTowerBlockEntity) {
        Block block = grenadeTowerBlockEntity.getBlockState().getBlock();
        if (block instanceof GrenadeTowerBlock b) {
            if (b.defaultBlockState().getValue(GrenadeTowerBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grenade_tower_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grenade_tower.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GrenadeTowerBlockEntity grenadeTowerBlockEntity) {
        return null;
    }

    @Override
    public RenderType getRenderType(GrenadeTowerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
