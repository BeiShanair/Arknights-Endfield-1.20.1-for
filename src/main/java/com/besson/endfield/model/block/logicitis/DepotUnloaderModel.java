package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.depot.DepotUnloaderBlock;
import com.besson.endfield.blockEntity.custom.depot.DepotUnloaderBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class DepotUnloaderModel extends GeoModel<DepotUnloaderBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DepotUnloaderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_unloader.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotUnloaderBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof DepotUnloaderBlock b) {
            if (b.defaultBlockState().getValue(DepotUnloaderBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_unloader_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_unloader.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotUnloaderBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(DepotUnloaderBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
