package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.logicitis.DepotBusSectionBlockEntity;
import com.besson.endfield.model.block.logicitis.DepotBusSectionModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DepotBusSectionRenderer extends GeoBlockRenderer<DepotBusSectionBlockEntity> {
    public DepotBusSectionRenderer(BlockEntityRendererProvider.Context context) {
        super(new DepotBusSectionModel());
    }

    @Override
    public boolean shouldRenderOffScreen(DepotBusSectionBlockEntity pBlockEntity) {
        return true;
    }
}
