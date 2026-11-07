package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production2.GrindingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.GrindingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class GrindingUnitModel extends GeoModel<GrindingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GrindingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/grinding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GrindingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof GrindingUnitBlock b) {
            if (b.defaultBlockState().getValue(GrindingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grinding_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/grinding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GrindingUnitBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(GrindingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
