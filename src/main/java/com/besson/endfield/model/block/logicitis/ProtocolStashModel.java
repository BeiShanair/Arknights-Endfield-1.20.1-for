package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.logicitis.ProtocolStashBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ProtocolStashModel extends GeoModel<ProtocolStashBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ProtocolStashBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/protocol_stash.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProtocolStashBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/protocol_stash.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ProtocolStashBlockEntity animatable) {
        return null;
    }
}
