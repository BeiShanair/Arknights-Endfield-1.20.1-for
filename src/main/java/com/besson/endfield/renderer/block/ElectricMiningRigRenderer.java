package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.ElectricMiningRigBlockEntity;
import com.besson.endfield.model.block.ElectricMiningRigModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ElectricMiningRigRenderer extends GeoBlockRenderer<ElectricMiningRigBlockEntity> {
    public ElectricMiningRigRenderer(BlockEntityRendererProvider.Context context) {
        super(new ElectricMiningRigModel());
    }
}
