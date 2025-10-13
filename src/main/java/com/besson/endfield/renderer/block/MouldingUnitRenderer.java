package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.MouldingUnitBlockEntity;
import com.besson.endfield.model.block.MouldingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MouldingUnitRenderer extends GeoBlockRenderer<MouldingUnitBlockEntity> {
    public MouldingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new MouldingUnitModel());
    }
}
