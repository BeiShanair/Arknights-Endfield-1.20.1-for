package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.logicitis.DepotUnloaderBlockEntity;
import com.besson.endfield.model.block.logicitis.DepotUnloaderModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DepotUnloaderRenderer extends GeoBlockRenderer<DepotUnloaderBlockEntity> {
    public DepotUnloaderRenderer(BlockEntityRendererProvider.Context context) {
        super(new DepotUnloaderModel());
    }

    @Override
    public boolean shouldRenderOffScreen(DepotUnloaderBlockEntity pBlockEntity) {
        return true;
    }
}
