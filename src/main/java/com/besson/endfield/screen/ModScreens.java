package com.besson.endfield.screen;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.screen.custom.CrafterScreenHandler;
import com.besson.endfield.screen.custom.PortableOriginiumRigScreenHandler;
import com.besson.endfield.screen.custom.ProtocolAnchorCoreScreenHandler;
import com.besson.endfield.screen.custom.ThermalBankScreenHandler;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModScreens {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<MenuType<CrafterScreenHandler>> CRAFTER_SCREEN =
            register("crafter", CrafterScreenHandler::new);
    public static final RegistryObject<MenuType<PortableOriginiumRigScreenHandler>> PORTABLE_ORIGINIUM_RIG_SCREEN =
            register("portable_originium_rig", PortableOriginiumRigScreenHandler::new);
    public static final RegistryObject<MenuType<ProtocolAnchorCoreScreenHandler>> PROTOCOL_ANCHOR_CORE_SCREEN =
            register("protocol_anchor_core", ProtocolAnchorCoreScreenHandler::new);
    public static final RegistryObject<MenuType<ThermalBankScreenHandler>> THERMAL_BANK_SCREEN =
            register("thermal_bank", ThermalBankScreenHandler::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
