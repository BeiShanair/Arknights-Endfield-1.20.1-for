package com.besson.endfield.renderer.block.resourcing;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.resourcing.HydroMiningRigBlockEntity;
import com.besson.endfield.model.block.resourcing.HydroMiningRigModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HydroMiningRigRenderer extends GeoBlockRenderer<HydroMiningRigBlockEntity> {
    public HydroMiningRigRenderer(BlockEntityRendererProvider.Context context) {
        super(new HydroMiningRigModel());
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
