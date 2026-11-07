package com.besson.endfield.model.block.powering;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.powering.ThermalBankBlock;
import com.besson.endfield.blockEntity.custom.powering.ThermalBankBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class ThermalBankModel extends GeoModel<ThermalBankBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ThermalBankBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/thermal_bank.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThermalBankBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof ThermalBankBlock b) {
            if (b.defaultBlockState().getValue(ThermalBankBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/thermal_bank_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/thermal_bank.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThermalBankBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(ThermalBankBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
