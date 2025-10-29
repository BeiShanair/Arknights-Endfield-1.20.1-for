package com.besson.endfield.renderer.block;

import com.besson.endfield.blockEntity.custom.ThermalBankBlockEntity;
import com.besson.endfield.model.block.ThermalBankModel;
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
