package com.besson.endfield.renderer.block.powering;

import com.besson.endfield.blockEntity.custom.powering.XiraniteRelayBlockEntity;
import com.besson.endfield.model.block.powering.XiraniteRelayModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class XiraniteRelayRenderer extends GeoBlockRenderer<XiraniteRelayBlockEntity> {
    public XiraniteRelayRenderer(BlockEntityRendererProvider.Context context) {
        super(new XiraniteRelayModel());
    }
}
