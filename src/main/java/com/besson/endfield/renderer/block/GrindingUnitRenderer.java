package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.GrindingUnitBlockEntity;
import com.besson.endfield.model.block.GrindingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GrindingUnitRenderer extends GeoBlockRenderer<GrindingUnitBlockEntity> {
    public GrindingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new GrindingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(GrindingUnitBlockEntity pBlockEntity) {
        return true;
    }
}
