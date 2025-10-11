package com.besson.endfield.renderer;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.renderer.block.*;
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
        event.registerBlockEntityRenderer(ModBlockEntities.RELAY_TOWER.get(),
                RelayTowerEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ELECTRIC_PYLON.get(),
                ElectricPylonEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ELECTRIC_MINING_RIG.get(),
                ElectricMiningRigRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ELECTRIC_MINING_RIG_MK_II.get(),
                ElectricMiningRigMkIIRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.REFINING_UNIT.get(),
                RefiningUnitRenderer::new);
    }
}
