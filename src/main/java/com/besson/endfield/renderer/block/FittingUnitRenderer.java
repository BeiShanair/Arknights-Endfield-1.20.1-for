package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.production1.FittingUnitBlockEntity;
import com.besson.endfield.model.block.production1.FittingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FittingUnitRenderer extends GeoBlockRenderer<FittingUnitBlockEntity> {
    public FittingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new FittingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(FittingUnitBlockEntity pBlockEntity) {
        return true;
    }
}
