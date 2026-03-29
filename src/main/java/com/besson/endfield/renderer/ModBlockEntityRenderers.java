package com.besson.endfield.renderer;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.renderer.block.*;
import com.besson.endfield.renderer.block.combat.*;
import com.besson.endfield.renderer.block.powering.ElectricPylonEntityRenderer;
import com.besson.endfield.renderer.block.powering.ProtocolAnchorCoreRenderer;
import com.besson.endfield.renderer.block.powering.RelayTowerEntityRenderer;
import com.besson.endfield.renderer.block.powering.ThermalBankRenderer;
import com.besson.endfield.renderer.block.resourcing.ElectricMiningRigMkIIRenderer;
import com.besson.endfield.renderer.block.resourcing.ElectricMiningRigRenderer;
import com.besson.endfield.renderer.block.resourcing.PortableOriginiumRigEntityRenderer;
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
        event.registerBlockEntityRenderer(ModBlockEntities.FILLING_UNIT.get(),
                FillingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FITTING_UNIT.get(),
                FittingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GEARING_UNIT.get(),
                GearingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GRINDING_UNIT.get(),
                GrindingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOULDING_UNIT.get(),
                MouldingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PACKAGING_UNIT.get(),
                PackagingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PLANTING_UNIT.get(),
                PlantingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SEED_PICKING_UNIT.get(),
                SeedPickingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SHREDDING_UNIT.get(),
                ShreddingUnitRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BELT.get(),
                BeltRenderer::new);
        
        event.registerBlockEntityRenderer(ModBlockEntities.BEAM_TOWER.get(), 
                BeamTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GRENADE_TOWER.get(),
                GrenadeTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GUN_TOWER.get(), 
                GunTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.HEAVY_GUN_TOWER.get(),
                HeavyGunTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.HE_GRENADE_TOWER.get(),
                HeGrenadeTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.LN_TOWER.get(),
                LNTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MARSH_GAS_MK_I.get(),
                MarshGasMkIBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MEDICAL_TOWER.get(),
                MedicalTowerRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.OMNIDIRECTIONAL_SONIC_TOWER.get(),
                OmnidirectionalSonicTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SENTRY_TOWER.get(),
                SentryTowerBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SURGE_TOWER.get(),
                SurgeTowerBlockRenderer::new);
    }
}
