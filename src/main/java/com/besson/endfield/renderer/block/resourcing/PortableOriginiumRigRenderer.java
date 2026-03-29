package com.besson.endfield.renderer.block.resourcing;

import com.besson.endfield.blockEntity.custom.resourcing.PortableOriginiumRigBlockEntity;
import com.besson.endfield.model.block.resourcing.PortableOriginiumModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PortableOriginiumRigRenderer extends GeoBlockRenderer<PortableOriginiumRigBlockEntity> {
    public PortableOriginiumRigRenderer(BlockEntityRendererProvider.Context context) {
        super(new PortableOriginiumModel());
    }
}
