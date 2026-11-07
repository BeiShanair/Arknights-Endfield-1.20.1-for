package com.besson.endfield.renderer.block;

import com.besson.endfield.Config;
import com.besson.endfield.blockEntity.custom.production2.ForgeOfTheSkyBlockEntity;
import com.besson.endfield.model.block.production2.ForgeOfTheSkyModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ForgeOfTheSkyRenderer extends GeoBlockRenderer<ForgeOfTheSkyBlockEntity> {
    public ForgeOfTheSkyRenderer(BlockEntityRendererProvider.Context context) {
        super(new ForgeOfTheSkyModel());
    }

    @Override
    public boolean shouldRenderOffScreen(ForgeOfTheSkyBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return Config.equipmentRenderDistance();
    }
}
