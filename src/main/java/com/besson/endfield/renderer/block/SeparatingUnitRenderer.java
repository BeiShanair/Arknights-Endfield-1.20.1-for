package com.besson.endfield.renderer.block;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.production2.SeparatingUnitBlockEntity;
import com.besson.endfield.model.block.production2.SeparatingUnitModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SeparatingUnitRenderer extends GeoBlockRenderer<SeparatingUnitBlockEntity> {
    public SeparatingUnitRenderer(BlockEntityRendererProvider.Context context) {
        super(new SeparatingUnitModel());
    }

    @Override
    public boolean shouldRenderOffScreen(SeparatingUnitBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
