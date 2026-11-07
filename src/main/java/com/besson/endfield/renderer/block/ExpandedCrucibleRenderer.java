package com.besson.endfield.renderer.block;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.production2.ExpandedCrucibleBlockEntity;
import com.besson.endfield.model.block.production2.ExpandedCrucibleModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ExpandedCrucibleRenderer extends GeoBlockRenderer<ExpandedCrucibleBlockEntity> {
    public ExpandedCrucibleRenderer(BlockEntityRendererProvider.Context context) {
        super(new ExpandedCrucibleModel());
    }

    @Override
    public boolean shouldRenderOffScreen(ExpandedCrucibleBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
