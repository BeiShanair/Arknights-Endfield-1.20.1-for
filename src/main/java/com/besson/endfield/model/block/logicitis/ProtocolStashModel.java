package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.depot.ProtocolStashBlock;
import com.besson.endfield.blockEntity.custom.depot.ProtocolStashBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class ProtocolStashModel extends GeoModel<ProtocolStashBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ProtocolStashBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/protocol_stash.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProtocolStashBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof ProtocolStashBlock b) {
            if (b.defaultBlockState().getValue(ProtocolStashBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/protocol_stash_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/protocol_stash.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ProtocolStashBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(ProtocolStashBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
