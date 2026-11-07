package com.besson.endfield.model.block.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production1.PlantingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.PlantingUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class PlantingUnitModel extends GeoModel<PlantingUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(PlantingUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/planting_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlantingUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof PlantingUnitBlock b) {
            if (b.defaultBlockState().getValue(PlantingUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/planting_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/planting_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PlantingUnitBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(PlantingUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
