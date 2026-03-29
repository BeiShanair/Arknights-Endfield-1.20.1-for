package com.besson.endfield.renderer.block.powering;

import com.besson.endfield.blockEntity.custom.powering.RelayTowerBlockEntity;
import com.besson.endfield.model.block.powering.RelayTowerModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RelayTowerRenderer extends GeoBlockRenderer<RelayTowerBlockEntity> {
    public RelayTowerRenderer(BlockEntityRendererProvider.Context context) {
        super(new RelayTowerModel());
    }
}
