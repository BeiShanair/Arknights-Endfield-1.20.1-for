package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.production2.GearingUnitBlockEntity;
import com.besson.endfield.model.block.production2.GearingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GearingUnitRenderer extends GeoBlockRenderer<GearingUnitBlockEntity> {
    public GearingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new GearingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(GearingUnitBlockEntity pBlockEntity) {
        return true;
    }
}
