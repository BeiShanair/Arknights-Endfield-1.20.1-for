package com.besson.endfield.model.block.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.powering.ProtocolAnchorCoreBlock;
import com.besson.endfield.blockEntity.custom.powering.ProtocolAnchorCoreBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class ProtocolAnchorCoreModel extends GeoModel<ProtocolAnchorCoreBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ProtocolAnchorCoreBlockEntity protocolAnchorCoreBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/protocol_anchor_core.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProtocolAnchorCoreBlockEntity protocolAnchorCoreBlockEntity) {
        Block block = protocolAnchorCoreBlockEntity.getBlockState().getBlock();
        if (block instanceof ProtocolAnchorCoreBlock b) {
            if (b.defaultBlockState().getValue(ProtocolAnchorCoreBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/protocol_anchor_core_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/protocol_anchor_core.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ProtocolAnchorCoreBlockEntity protocolAnchorCoreBlockEntity) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/protocol_anchor_core.animation.json");
    }

    @Override
    public RenderType getRenderType(ProtocolAnchorCoreBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
