package com.besson.endfield.model.block.production2;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.custom.production2.ForgeOfTheSkyBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ForgeOfTheSkyModel extends GeoModel<ForgeOfTheSkyBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ForgeOfTheSkyBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "geo/forge_of_the_sky.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ForgeOfTheSkyBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "textures/block/forge_of_the_sky.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ForgeOfTheSkyBlockEntity animatable) {
        return null;
    }

    @Override
    public RenderType getRenderType(ForgeOfTheSkyBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
