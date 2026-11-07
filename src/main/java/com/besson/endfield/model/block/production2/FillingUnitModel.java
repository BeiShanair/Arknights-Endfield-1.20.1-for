package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production2.FillingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.FillingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class FillingUnitModel extends GeoModel<FillingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FillingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/filling_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FillingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof FillingUnitBlock b) {
            if (b.defaultBlockState().getValue(FillingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/filling_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/filling_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FillingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/filling_unit.animation.json");
    }

    @Override
    public RenderType getRenderType(FillingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
