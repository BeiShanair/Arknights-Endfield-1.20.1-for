package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.PackagingUnitBlockEntity;
import com.besson.endfield.model.block.PackagingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PackagingUnitRenderer extends GeoBlockRenderer<PackagingUnitBlockEntity> {
    public PackagingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new PackagingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(PackagingUnitBlockEntity pBlockEntity) {
        return true;
    }
}
