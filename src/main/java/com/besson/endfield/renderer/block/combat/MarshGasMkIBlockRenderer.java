package com.besson.endfield.renderer.block.combat;

import com.besson.endfield.blockEntity.custom.combat.MarshGasMkIBlockEntity;
import com.besson.endfield.model.block.combat.MarshGasMkIModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MarshGasMkIBlockRenderer extends GeoBlockRenderer<MarshGasMkIBlockEntity> {
    public MarshGasMkIBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new MarshGasMkIModel());
    }
    

    @Override
    public void actuallyRender(PoseStack poseStack, MarshGasMkIBlockEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        GeoBone turretBone = getGeoModel().getBone("gunSupport").orElse(null);
        GeoBone barrelBone = getGeoModel().getBone("gun").orElse(null);

        if (turretBone != null) {
            turretBone.setRotY((float) Math.toRadians(animatable.getTurretYaw()));
        }
        if (barrelBone != null) {
            barrelBone.setRotX((float) Math.toRadians(animatable.getTurretPitch()));
        }
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
