package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production2.GearingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.GearingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class GearingUnitModel extends GeoModel<GearingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GearingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/gearing_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GearingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof GearingUnitBlock b) {
            if (b.defaultBlockState().getValue(GearingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gearing_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/gearing_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GearingUnitBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(GearingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
