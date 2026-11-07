package com.besson.endfield.renderer.block;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.production1.ShreddingUnitBlockEntity;
import com.besson.endfield.model.block.production1.ShreddingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ShreddingUnitRenderer extends GeoBlockRenderer<ShreddingUnitBlockEntity> {
    public ShreddingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new ShreddingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(ShreddingUnitBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
