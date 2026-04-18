package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.logicitis.DepotLoaderBlockEntity;
import com.besson.endfield.model.block.logicitis.DepotLoaderModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DepotLoaderRenderer extends GeoBlockRenderer<DepotLoaderBlockEntity> {
    public DepotLoaderRenderer(BlockEntityRendererProvider.Context context) {
        super(new DepotLoaderModel());
    }

    @Override
    public boolean shouldRenderOffScreen(DepotLoaderBlockEntity pBlockEntity) {
        return true;
    }
}
