package com.besson.endfield.model.item.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.production2.ForgeOfTheSkyItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ForgeOfTheSkyItemModel extends GeoModel<ForgeOfTheSkyItem> {
    @Override
    public ResourceLocation getModelResource(ForgeOfTheSkyItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/forge_of_the_sky.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ForgeOfTheSkyItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/forge_of_the_sky.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ForgeOfTheSkyItem animatable) {
        return null;
    }
}
