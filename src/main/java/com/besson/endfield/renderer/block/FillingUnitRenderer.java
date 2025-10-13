package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.FillingUnitBlockEntity;
import com.besson.endfield.model.block.FillingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FillingUnitRenderer extends GeoBlockRenderer<FillingUnitBlockEntity> {
    public FillingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new FillingUnitModel());
    }
}
