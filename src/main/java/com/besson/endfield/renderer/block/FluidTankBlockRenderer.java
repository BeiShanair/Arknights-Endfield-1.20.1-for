package com.besson.endfield.renderer.block;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.depot.FluidTankBlockEntity;
import com.besson.endfield.model.block.FluidTankModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FluidTankBlockRenderer extends GeoBlockRenderer<FluidTankBlockEntity> {
    public FluidTankBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new FluidTankModel());
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
