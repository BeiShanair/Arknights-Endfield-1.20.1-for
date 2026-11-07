package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.combat.MarshGasMkIBlock;
import com.besson.endfield.blockEntity.custom.combat.MarshGasMkIBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class MarshGasMkIModel extends GeoModel<MarshGasMkIBlockEntity> {
    @Override
    public ResourceLocation getModelResource(MarshGasMkIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/marsh_gas_mk_i.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarshGasMkIBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        if (block instanceof MarshGasMkIBlock b) {
            if (b.defaultBlockState().getValue(MarshGasMkIBlock.PREVIEW)) {
                return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/marsh_gas_mk_i_pre.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/marsh_gas_mk_i.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarshGasMkIBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(MarshGasMkIBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
