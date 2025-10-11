package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.ElectricMiningRigMkIIBlockEntity;
import com.besson.endfield.model.block.ElectricMiningRigMkIIModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ElectricMiningRigMkIIRenderer extends GeoBlockRenderer<ElectricMiningRigMkIIBlockEntity> {
    public ElectricMiningRigMkIIRenderer(BlockEntityRendererProvider.Context context) {
        super(new ElectricMiningRigMkIIModel());
    }
}
