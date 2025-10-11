package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.RefiningUnitBlockEntity;
import com.besson.endfield.model.block.RefiningUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RefiningUnitRenderer extends GeoBlockRenderer<RefiningUnitBlockEntity> {
    public RefiningUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new RefiningUnitModel());
    }
}
