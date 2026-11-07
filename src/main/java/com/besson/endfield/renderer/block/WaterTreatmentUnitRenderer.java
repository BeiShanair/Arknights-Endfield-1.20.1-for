package com.besson.endfield.renderer.block;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.production1.WaterTreatmentUnitBlockEntity;
import com.besson.endfield.model.block.production1.WaterTreatmentUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class WaterTreatmentUnitRenderer extends GeoBlockRenderer<WaterTreatmentUnitBlockEntity> {
    public WaterTreatmentUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new WaterTreatmentUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(WaterTreatmentUnitBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
