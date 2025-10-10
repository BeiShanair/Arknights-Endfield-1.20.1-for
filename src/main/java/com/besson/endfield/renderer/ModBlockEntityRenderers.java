package com.besson.endfield.renderer;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.renderer.block.PortableOriginiumRigEntityRenderer;
import com.besson.endfield.renderer.block.ProtocolAnchorCoreRenderer;
import com.besson.endfield.renderer.block.ThermalBankRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArknightsEndfield.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlockEntityRenderers {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.PORTABLE_ORIGINIUM_RIG.get(),
                PortableOriginiumRigEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PROTOCOL_ANCHOR_CORE.get(),
                ProtocolAnchorCoreRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.THERMAL_BANK.get(),
                ThermalBankRenderer::new);
    }
}
