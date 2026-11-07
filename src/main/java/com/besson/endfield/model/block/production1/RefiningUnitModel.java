package com.besson.endfield.model.block.production1;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.production1.RefiningUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.RefiningUnitBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class RefiningUnitModel extends GeoModel<RefiningUnitBlockEntity> {
    @Override
    public ResourceLocation getModelResource(RefiningUnitBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/refining_unit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RefiningUnitBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof RefiningUnitBlock b) {
            if (b.defaultBlockState().getValue(RefiningUnitBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/refining_unit_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/refining_unit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RefiningUnitBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(RefiningUnitBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
