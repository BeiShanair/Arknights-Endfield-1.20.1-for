package com.besson.endfield.renderer.block.combat;

import com.besson.endfield.blockEntity.custom.combat.OmnidirectionalSonicTowerBlockEntity;
import com.besson.endfield.model.block.combat.OmnidirectionalSonicTowerModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class OmnidirectionalSonicTowerBlockRenderer extends GeoBlockRenderer<OmnidirectionalSonicTowerBlockEntity> {
    public OmnidirectionalSonicTowerBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new OmnidirectionalSonicTowerModel());
    }
}
