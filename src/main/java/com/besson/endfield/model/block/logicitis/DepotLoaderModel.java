package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.depot.DepotLoaderBlock;
import com.besson.endfield.blockEntity.custom.depot.DepotLoaderBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class DepotLoaderModel extends GeoModel<DepotLoaderBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DepotLoaderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_loader.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotLoaderBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof DepotLoaderBlock b) {
            if (b.defaultBlockState().getValue(DepotLoaderBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_loader_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_loader.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotLoaderBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(DepotLoaderBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
