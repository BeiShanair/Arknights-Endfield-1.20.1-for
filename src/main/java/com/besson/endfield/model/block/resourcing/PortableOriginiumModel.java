package com.besson.endfield.model.block.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.resourcing.PortableOriginiumRigBlock;
import com.besson.endfield.blockEntity.custom.resourcing.PortableOriginiumRigBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class PortableOriginiumModel extends GeoModel<PortableOriginiumRigBlockEntity> {
    @Override
    public ResourceLocation getModelResource(PortableOriginiumRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/portable_originium_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PortableOriginiumRigBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof PortableOriginiumRigBlock b) {
            if (b.defaultBlockState().getValue(PortableOriginiumRigBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/portable_originium_rig_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/portable_originium_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PortableOriginiumRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/portable_originium_rig.animation.json");
    }

    @Override
    public RenderType getRenderType(PortableOriginiumRigBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
