package com.besson.endfield.model.block.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.powering.ElectricPylonBlock;
import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class ElectricPylonModel extends GeoModel<ElectricPylonBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ElectricPylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_pylon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricPylonBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof ElectricPylonBlock b) {
            if (b.defaultBlockState().getValue(ElectricPylonBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_pylon_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_pylon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricPylonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_pylon.animation.json");
    }

    @Override
    public RenderType getRenderType(ElectricPylonBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
