package com.besson.endfield.model.block.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production1.ShreddingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.ShreddingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class ShreddingUnitModel extends GeoModel<ShreddingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ShreddingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/shredding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShreddingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof ShreddingUnitBlock b) {
            if (b.defaultBlockState().getValue(ShreddingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/shredding_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/shredding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShreddingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/shredding_unit.animation.json");
    }

    @Override
    public RenderType getRenderType(ShreddingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
