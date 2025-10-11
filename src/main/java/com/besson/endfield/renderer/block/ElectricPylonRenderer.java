package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.ElectricPylonBlockEntity;
import com.besson.endfield.model.block.ElectricPylonModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ElectricPylonRenderer extends GeoBlockRenderer<ElectricPylonBlockEntity> {
    public ElectricPylonRenderer(BlockEntityRendererProvider.Context context) {
        super(new ElectricPylonModel());
    }
}
