package com.besson.endfield.model.block.logicitis;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.depot.DepotBusSectionBlock;
import com.besson.endfield.blockEntity.custom.depot.DepotBusSectionBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class DepotBusSectionModel extends GeoModel<DepotBusSectionBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DepotBusSectionBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/depot_bus_section.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DepotBusSectionBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof DepotBusSectionBlock b) {
            if (b.defaultBlockState().getValue(DepotBusSectionBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_bus_section_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/depot_bus_section.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DepotBusSectionBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(DepotBusSectionBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
