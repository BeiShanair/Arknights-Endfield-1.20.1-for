package com.besson.endfield.renderer.block;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.production2.ReactorCrucibleBlockEntity;
import com.besson.endfield.model.block.production2.ReactorCrucibleModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ReactorCrucibleRenderer extends GeoBlockRenderer<ReactorCrucibleBlockEntity> {
    public ReactorCrucibleRenderer(BlockEntityRendererProvider.Context context) {
        super(new ReactorCrucibleModel());
    }

    @Override
    public boolean shouldRenderOffScreen(ReactorCrucibleBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
