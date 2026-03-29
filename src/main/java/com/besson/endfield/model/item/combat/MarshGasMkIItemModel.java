package com.besson.endfield.model.item.combat;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.combat.MarshGasMkIItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MarshGasMkIItemModel extends GeoModel<MarshGasMkIItem> {
    @Override
    public ResourceLocation getModelResource(MarshGasMkIItem animatable) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "geo/marsh_gas_mk_i.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarshGasMkIItem animatable) {
        return new ResourceLocation(ArknightsEndfield.MOD_ID, "textures/block/marsh_gas_mk_i.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarshGasMkIItem animatable) {
        return null;
    }
}
