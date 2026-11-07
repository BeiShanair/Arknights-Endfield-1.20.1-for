package com.besson.endfield.renderer.block.powering;

import com.besson.endfield.blockEntity.custom.powering.XiranitePylonBlockEntity;
import com.besson.endfield.model.block.powering.XiranitePylonModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class XiranitePylonRenderer extends GeoBlockRenderer<XiranitePylonBlockEntity> {
    public XiranitePylonRenderer(BlockEntityRendererProvider.Context context) {
        super(new XiranitePylonModel());
    }
}
