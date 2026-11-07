package com.besson.endfield.renderer.block.combat;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.combat.BeamTowerBlockEntity;
import com.besson.endfield.model.block.combat.BeamTowerModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BeamTowerBlockRenderer extends GeoBlockRenderer<BeamTowerBlockEntity> {
    public BeamTowerBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new BeamTowerModel());
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
