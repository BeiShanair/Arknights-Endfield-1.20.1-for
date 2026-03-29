package com.besson.endfield.model.block.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.combat.MarshGasMkIBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MarshGasMkIModel extends GeoModel<MarshGasMkIBlockEntity> {
    @Override
    public ResourceLocation getModelResource(MarshGasMkIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/marsh_gas_mk_i.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarshGasMkIBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/marsh_gas_mk_i.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarshGasMkIBlockEntity animatable) {
        return null;
    }
}
