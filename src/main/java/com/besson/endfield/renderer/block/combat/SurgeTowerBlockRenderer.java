package com.besson.endfield.renderer.block.combat;

import com.besson.endfield.blockEntity.custom.combat.SurgeTowerBlockEntity;
import com.besson.endfield.model.block.combat.SurgeTowerModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SurgeTowerBlockRenderer extends GeoBlockRenderer<SurgeTowerBlockEntity> {
    public SurgeTowerBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new SurgeTowerModel());
    }
}
