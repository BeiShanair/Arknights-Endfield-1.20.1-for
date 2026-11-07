package com.besson.endfield.model.block.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production1.FittingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.FittingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class FittingUnitModel extends GeoModel<FittingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(FittingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/fitting_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FittingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof FittingUnitBlock b) {
            if (b.defaultBlockState().getValue(FittingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fitting_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/fitting_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FittingUnitBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(FittingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
