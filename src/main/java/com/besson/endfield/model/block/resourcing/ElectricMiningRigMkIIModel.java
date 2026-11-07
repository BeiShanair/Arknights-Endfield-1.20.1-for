package com.besson.endfield.model.block.resourcing;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.resourcing.ElectricMiningRigMkIIBlock;
import com.besson.endfield.blockEntity.custom.resourcing.ElectricMiningRigMkIIBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class ElectricMiningRigMkIIModel extends GeoModel<ElectricMiningRigMkIIBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ElectricMiningRigMkIIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/electric_mining_rig_mk_ii.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricMiningRigMkIIBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof ElectricMiningRigMkIIBlock b) {
            if (b.defaultBlockState().getValue(ElectricMiningRigMkIIBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig_mk_ii_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/electric_mining_rig_mk_ii.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricMiningRigMkIIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "animations/electric_mining_rig_mk_ii.animation.json");
    }

    @Override
    public RenderType getRenderType(ElectricMiningRigMkIIBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
