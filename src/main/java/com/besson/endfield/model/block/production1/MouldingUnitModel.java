package com.besson.endfield.model.block.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production1.MouldingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.MouldingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class MouldingUnitModel extends GeoModel<MouldingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(MouldingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/moulding_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MouldingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof MouldingUnitBlock b) {
            if (b.defaultBlockState().getValue(MouldingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/moulding_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/moulding_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MouldingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/moulding_unit.animation.json");
    }

    @Override
    public RenderType getRenderType(MouldingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
