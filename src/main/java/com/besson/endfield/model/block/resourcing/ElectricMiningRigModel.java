package com.besson.endfield.model.block.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.resourcing.ElectricMiningRigBlock;
import com.besson.endfield.blockEntity.custom.resourcing.ElectricMiningRigBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class ElectricMiningRigModel extends GeoModel<ElectricMiningRigBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ElectricMiningRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_mining_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricMiningRigBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof ElectricMiningRigBlock b) {
            if (b.defaultBlockState().getValue(ElectricMiningRigBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricMiningRigBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_mining_rig.animation.json");
    }

    @Override
    public RenderType getRenderType(ElectricMiningRigBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
