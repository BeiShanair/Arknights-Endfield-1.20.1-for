package com.besson.endfield.renderer.block.powering;

import com.besson.endfield.blockEntity.custom.powering.ThermalBankBlockEntity;
import com.besson.endfield.model.block.powering.ThermalBankModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ThermalBankRenderer extends GeoBlockRenderer<ThermalBankBlockEntity> {
    public ThermalBankRenderer(BlockEntityRendererProvider.Context context) {
        super(new ThermalBankModel());
    }

    @Override
    public boolean shouldRenderOffScreen(ThermalBankBlockEntity pBlockEntity) {
        return true;
    }
}
