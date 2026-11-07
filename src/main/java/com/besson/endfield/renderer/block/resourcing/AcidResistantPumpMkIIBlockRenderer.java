package com.besson.endfield.renderer.block.resourcing;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.resourcing.AcidResistantPumpMkIIBlockEntity;
import com.besson.endfield.model.block.resourcing.AcidResistantPumpMkIIBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AcidResistantPumpMkIIBlockRenderer extends GeoBlockRenderer<AcidResistantPumpMkIIBlockEntity> {
    public AcidResistantPumpMkIIBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new AcidResistantPumpMkIIBlockModel());
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
