package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.production1.PlantingUnitBlockEntity;
import com.besson.endfield.model.block.production1.PlantingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PlantingUnitRenderer extends GeoBlockRenderer<PlantingUnitBlockEntity> {
    public PlantingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new PlantingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(PlantingUnitBlockEntity pBlockEntity) {
        return true;
    }
}
