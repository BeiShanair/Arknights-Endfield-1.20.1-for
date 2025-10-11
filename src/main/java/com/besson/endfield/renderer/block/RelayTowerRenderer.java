package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.RelayTowerBlockEntity;
import com.besson.endfield.model.block.RelayTowerModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RelayTowerRenderer extends GeoBlockRenderer<RelayTowerBlockEntity> {
    public RelayTowerRenderer(BlockEntityRendererProvider.Context context) {
        super(new RelayTowerModel());
    }
}
