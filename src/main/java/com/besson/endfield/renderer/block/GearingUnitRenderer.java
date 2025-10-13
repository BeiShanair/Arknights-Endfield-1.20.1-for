package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.GearingUnitBlockEntity;
import com.besson.endfield.model.block.GearingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GearingUnitRenderer extends GeoBlockRenderer<GearingUnitBlockEntity> {
    public GearingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new GearingUnitModel());
    }
}
