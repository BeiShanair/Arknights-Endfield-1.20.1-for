package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.ProtocolAnchorCoreBlockEntity;
import com.besson.endfield.model.block.ProtocolAnchorCoreModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ProtocolAnchorCoreRenderer extends GeoBlockRenderer<ProtocolAnchorCoreBlockEntity> {
    public ProtocolAnchorCoreRenderer(BlockEntityRendererProvider.Context context) {
        super(new ProtocolAnchorCoreModel());
    }

    @Override
    public boolean shouldRenderOffScreen(ProtocolAnchorCoreBlockEntity pBlockEntity) {
        return true;
    }
}
