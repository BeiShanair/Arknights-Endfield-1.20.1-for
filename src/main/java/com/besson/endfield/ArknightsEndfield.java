package com.besson.endfield;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.entity.ModItemEntity;
import com.besson.endfield.fluid.ModFluidTypes;
import com.besson.endfield.fluid.ModFluids;
import com.besson.endfield.item.ModItemGroups;
import com.besson.endfield.item.ModItems;
import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.recipe.ModRecipes;
import com.besson.endfield.renderer.pre.combat.*;
import com.besson.endfield.renderer.pre.logicitis.DepotBusSectionPreviewRenderer;
import com.besson.endfield.renderer.pre.logicitis.DepotLoaderPreviewRenderer;
import com.besson.endfield.renderer.pre.logicitis.DepotUnloaderPreviewRenderer;
import com.besson.endfield.renderer.pre.logicitis.ProtocolStashPreviewRenderer;
import com.besson.endfield.renderer.pre.powering.ElectricPylonPreviewRenderer;
import com.besson.endfield.renderer.pre.powering.ProtocolAnchorCorePreviewRenderer;
import com.besson.endfield.renderer.pre.powering.RelayTowerPreviewRenderer;
import com.besson.endfield.renderer.pre.powering.ThermalBankPreviewRenderer;
import com.besson.endfield.renderer.pre.production1.*;
import com.besson.endfield.renderer.pre.production2.FillingUnitPreviewRenderer;
import com.besson.endfield.renderer.pre.production2.GearingUnitPreviewRenderer;
import com.besson.endfield.renderer.pre.production2.GrindingUnitPreviewRenderer;
import com.besson.endfield.renderer.pre.production2.PackagingUnitPreviewRenderer;
import com.besson.endfield.renderer.pre.resourcing.ElectricMiningRigMkIIPreviewRenderer;
import com.besson.endfield.renderer.pre.resourcing.ElectricMiningRigPreviewRenderer;
import com.besson.endfield.renderer.pre.resourcing.PortableOriginiumRigPreviewRenderer;
import com.besson.endfield.screen.ModScreens;
import com.besson.endfield.screen.custom.*;
import com.besson.endfield.screen.custom.logicitis.DepotUnloaderScreen;
import com.besson.endfield.screen.custom.logicitis.ProtocolStashScreen;
import com.besson.endfield.screen.custom.logicitis.StorageScreen;
import com.besson.endfield.screen.custom.powering.ProtocolAnchorCorePortScreen;
import com.besson.endfield.screen.custom.powering.ProtocolAnchorCoreScreen;
import com.besson.endfield.screen.custom.powering.ThermalBankScreen;
import com.besson.endfield.screen.custom.production1.*;
import com.besson.endfield.screen.custom.production2.ExpandedCrucibleScreen;
import com.besson.endfield.screen.custom.production2.FillingUnitScreen;
import com.besson.endfield.screen.custom.production2.ForgeOfTheSkyScreen;
import com.besson.endfield.screen.custom.production2.GearingUnitScreen;
import com.besson.endfield.screen.custom.production2.GrindingUnitScreen;
import com.besson.endfield.screen.custom.production2.PackagingUnitScreen;
import com.besson.endfield.screen.custom.production2.ReactorCrucibleScreen;
import com.besson.endfield.screen.custom.production2.SeparatingUnitScreen;
import com.besson.endfield.screen.custom.resourcing.*;
import com.besson.endfield.util.ModKeyBindings;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(ArknightsEndfield.MOD_ID)
public class ArknightsEndfield {
    public static final String MOD_ID = "arknights_endfield";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArknightsEndfield(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        GeckoLib.initialize();

        ModItems.register(modEventBus);
        ModItemGroups.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModScreens.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModItemEntity.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);

        ModNetWorking.register();
        
        context.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
        event.enqueueWork(ModItems::bindBottledVariants);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SuppressWarnings({"removal", "deprecation"})
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            event.enqueueWork(() -> {
                MenuScreens.register(ModScreens.CRAFTER_SCREEN.get(), CrafterScreen::new);
                MenuScreens.register(ModScreens.PORTABLE_ORIGINIUM_RIG_SCREEN.get(), PortableOriginiumRigScreen::new);
                MenuScreens.register(ModScreens.PROTOCOL_ANCHOR_CORE_SCREEN.get(), ProtocolAnchorCoreScreen::new);
                MenuScreens.register(ModScreens.THERMAL_BANK_SCREEN.get(), ThermalBankScreen::new);
                MenuScreens.register(ModScreens.ELECTRIC_MINING_RIG_SCREEN.get(), ElectricMiningRigScreen::new);
                MenuScreens.register(ModScreens.HYDRO_MINING_RIG_SCREEN.get(), HydroMiningRigScreen::new);
                MenuScreens.register(ModScreens.WATER_TREATMENT_UNIT_SCREEN.get(), WaterTreatmentUnitScreen::new);
                MenuScreens.register(ModScreens.ELECTRIC_MINING_RIG_MK_II_SCREEN.get(), ElectricMiningRigMkIIScreen::new);
                MenuScreens.register(ModScreens.REFINING_UNIT_SCREEN.get(), RefiningUnitScreen::new);
                MenuScreens.register(ModScreens.FILLING_UNIT_SCREEN.get(), FillingUnitScreen::new);
                MenuScreens.register(ModScreens.FITTING_UNIT_SCREEN.get(), FittingUnitScreen::new);
                MenuScreens.register(ModScreens.GEARING_UNIT_SCREEN.get(), GearingUnitScreen::new);
                MenuScreens.register(ModScreens.GRINDING_UNIT_SCREEN.get(), GrindingUnitScreen::new);
                MenuScreens.register(ModScreens.MOULDING_UNIT_SCREEN.get(), MouldingUnitScreen::new);
                MenuScreens.register(ModScreens.PACKAGING_UNIT_SCREEN.get(), PackagingUnitScreen::new);
                MenuScreens.register(ModScreens.REACTOR_CRUCIBLE_SCREEN.get(), ReactorCrucibleScreen::new);
                MenuScreens.register(ModScreens.EXPANDED_CRUCIBLE_SCREEN.get(), ExpandedCrucibleScreen::new);
                MenuScreens.register(ModScreens.SEPARATING_UNIT_SCREEN.get(), SeparatingUnitScreen::new);
                MenuScreens.register(ModScreens.FORGE_OF_THE_SKY_SCREEN.get(), ForgeOfTheSkyScreen::new);
                MenuScreens.register(ModScreens.PLANTING_UNIT_SCREEN.get(), PlantingUnitScreen::new);
                MenuScreens.register(ModScreens.SEED_PICKING_UNIT_SCREEN.get(), SeedPickingUnitScreen::new);
                MenuScreens.register(ModScreens.SHREDDING_UNIT_SCREEN.get(), ShreddingUnitScreen::new);
                MenuScreens.register(ModScreens.DEPOT_UNLOADER_SCREEN.get(), DepotUnloaderScreen::new);
                MenuScreens.register(ModScreens.PROTOCOL_STASH_SCREEN.get(), ProtocolStashScreen::new);
                MenuScreens.register(ModScreens.PROTOCOL_ANCHOR_CORE_PORT_SCREEN.get(), ProtocolAnchorCorePortScreen::new);
                MenuScreens.register(ModScreens.STORAGE_SCREEN.get(), StorageScreen::new);
                MenuScreens.register(ModScreens.FLUID_PUMP_SCREEN.get(), FluidPumpScreen::new);
                MenuScreens.register(ModScreens.ACID_RESISTANT_PUMP_MK_II_SCREEN.get(), AcidResistantPumpMkIIScreen::new);
                MenuScreens.register(ModScreens.FLUID_TANK_SCREEN.get(), FluidTankScreen::new);

                ItemBlockRenderTypes.setRenderLayer(ModBlocks.AKETINE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_AKETINE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.AMBER_RICE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_AMBER_RICE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.BUCKFLOWER_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_BUCKFLOWER_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.CITROME_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_CITROME_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.FIREBUCKLE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_FIREBUCKLE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUFFED_JINCAO_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_FLUFFED_JINCAO_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.JINCAO_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_JINCAO_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.REDJADE_GINSENG_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_REDJADE_GINSENG_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.REED_RYE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_REED_RYE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.SANDLEAF_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_SANDLEAF_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.TARTPEPPER_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_TARTPEPPER_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.THORNY_YAZHEN_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_THORNY_YAZHEN_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.UMBRALINE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_UMBRALINE_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.YAZHEN_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_YAZHEN_BLOCK.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_PIPE.get(), RenderType.cutout());

                // 传送带的渲染层级由客户端配置 beltRenderLayer 决定（半透明 / 镂空）
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.BELT.get(),
                        Config.beltRenderLayer() == Config.BeltRenderLayer.CUTOUT
                                ? RenderType.cutout()
                                : RenderType.translucent());
                
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SEWAGE.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.STILL_SEWAGE.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PRECIPITATION_ACID.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.STILL_PRECIPITATION_ACID.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_XIRCON_EFFLUENT.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.STILL_XIRCON_EFFLUENT.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_INERT_XIRCON_EFFLUENT.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.STILL_INERT_XIRCON_EFFLUENT.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_YAZHEN_SOLUTION.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.STILL_YAZHEN_SOLUTION.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_JINCAO_SOLUTION.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.STILL_JINCAO_SOLUTION.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_LIQUID_XIRANITE.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.STILL_LIQUID_XIRANITE.get(), RenderType.translucent());

                ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_PIPE.get(), RenderType.cutout());

                DepotBusSectionPreviewRenderer.register();
                DepotLoaderPreviewRenderer.register();
                DepotUnloaderPreviewRenderer.register();
                ProtocolStashPreviewRenderer.register();

                ElectricPylonPreviewRenderer.register();
                ProtocolAnchorCorePreviewRenderer.register();
                RelayTowerPreviewRenderer.register();
                ThermalBankPreviewRenderer.register();

                BeamTowerPreviewRenderer.register();
                GrenadeTowerPreviewRenderer.register();
                GunTowerPreviewRenderer.register();
                HeavyGunTowerPreviewRenderer.register();
                HeGrenadeTowerPreviewRenderer.register();
                LNTowerPreviewRenderer.register();
                MarshGasMkIPreviewRenderer.register();
                MedicalTowerPreviewRenderer.register();
                OmnidirectionalSonicTowerPreviewRenderer.register();
                SentryTowerPreviewRenderer.register();
                SurgeTowerPreviewRenderer.register();

                FittingUnitPreviewRenderer.register();
                MouldingUnitPreviewRenderer.register();
                PlantingUnitPreviewRenderer.register();
                RefiningUnitPreviewRenderer.register();
                SeedPickingUnitPreviewRenderer.register();
                ShreddingUnitPreviewRenderer.register();

                FillingUnitPreviewRenderer.register();
                GearingUnitPreviewRenderer.register();
                GrindingUnitPreviewRenderer.register();
                PackagingUnitPreviewRenderer.register();

                ElectricMiningRigPreviewRenderer.register();
                ElectricMiningRigMkIIPreviewRenderer.register();
                PortableOriginiumRigPreviewRenderer.register();
                
            });
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(ModItemEntity.INDUSTRIAL_EXPLOSIVE.get(), ThrownItemRenderer::new);
        }
        
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event){
            ModKeyBindings.register(event);
        }
    }
}
