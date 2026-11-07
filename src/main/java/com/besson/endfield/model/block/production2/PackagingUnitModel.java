package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production2.PackagingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.PackagingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class PackagingUnitModel extends GeoModel<PackagingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(PackagingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/packaging_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PackagingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof PackagingUnitBlock b) {
            if (b.defaultBlockState().getValue(PackagingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/packaging_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/packaging_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PackagingUnitBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(PackagingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
