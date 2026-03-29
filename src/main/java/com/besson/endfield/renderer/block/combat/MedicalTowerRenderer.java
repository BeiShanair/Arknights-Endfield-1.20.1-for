package com.besson.endfield.renderer.block.combat;

import com.besson.endfield.blockEntity.custom.combat.MedicalTowerBlockEntity;
import com.besson.endfield.model.block.combat.MedicalTowerModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MedicalTowerRenderer extends GeoBlockRenderer<MedicalTowerBlockEntity> {
    public MedicalTowerRenderer(BlockEntityRendererProvider.Context context) {
        super(new MedicalTowerModel());
    }
}
