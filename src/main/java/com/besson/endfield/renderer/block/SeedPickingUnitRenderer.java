package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.SeedPickingUnitBlockEntity;
import com.besson.endfield.model.block.SeedPickingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SeedPickingUnitRenderer extends GeoBlockRenderer<SeedPickingUnitBlockEntity> {
    public SeedPickingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new SeedPickingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(SeedPickingUnitBlockEntity pBlockEntity) {
        return true;
    }
}
