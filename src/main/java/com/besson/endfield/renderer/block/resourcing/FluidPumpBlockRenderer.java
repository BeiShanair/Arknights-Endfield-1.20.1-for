package com.besson.endfield.renderer.block.resourcing;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.resourcing.FluidPumpBlockEntity;
import com.besson.endfield.model.block.resourcing.FluidPumpBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FluidPumpBlockRenderer extends GeoBlockRenderer<FluidPumpBlockEntity> {
    public FluidPumpBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new FluidPumpBlockModel());
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
