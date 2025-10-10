package com.besson.endfield;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.item.ModItemGroups;
import com.besson.endfield.item.ModItems;
import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.recipe.ModRecipes;
import com.besson.endfield.screen.ModScreens;
import com.besson.endfield.screen.custom.CrafterScreen;
import com.besson.endfield.screen.custom.PortableOriginiumRigScreen;
import com.besson.endfield.screen.custom.ProtocolAnchorCoreScreen;
import com.besson.endfield.screen.custom.ThermalBankScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
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

        ModNetWorking.register();

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
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
            });
        }
    }
}
