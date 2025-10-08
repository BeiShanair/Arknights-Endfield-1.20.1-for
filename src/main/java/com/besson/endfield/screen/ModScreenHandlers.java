package com.besson.endfield.screen;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.screen.custom.CrafterScreenHandler;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModScreenHandlers {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<MenuType<CrafterScreenHandler>> CRAFTER_SCREEN =
            MENU_TYPES.register("crafter", () -> IForgeMenuType.create(CrafterScreenHandler::new));

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> register(IContainerFactory<T> factory, String name) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
