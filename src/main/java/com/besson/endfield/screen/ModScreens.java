package com.besson.endfield.screen;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.screen.custom.*;
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
    public static final RegistryObject<MenuType<ElectricMiningRigScreenHandler>> ELECTRIC_MINING_RIG_SCREEN =
            register("electric_mining_rig", ElectricMiningRigScreenHandler::new);
    public static final RegistryObject<MenuType<ElectricMiningRigMkIIScreenHandler>> ELECTRIC_MINING_RIG_MK_II_SCREEN =
            register("electric_mining_rig_mk_ii", ElectricMiningRigMkIIScreenHandler::new);
    public static final RegistryObject<MenuType<RefiningUnitScreenHandler>> REFINING_UNIT_SCREEN =
            register("refining_unit", RefiningUnitScreenHandler::new);
    public static final RegistryObject<MenuType<FillingUnitScreenHandler>> FILLING_UNIT_SCREEN =
            register("filling_unit", FillingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<FittingUnitScreenHandler>> FITTING_UNIT_SCREEN =
            register("fitting_unit", FittingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<GearingUnitScreenHandler>> GEARING_UNIT_SCREEN =
            register("gearing_unit", GearingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<GrindingUnitScreenHandler>> GRINDING_UNIT_SCREEN =
            register("grinding_unit", GrindingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<MouldingUnitScreenHandler>> MOULDING_UNIT_SCREEN =
            register("moulding_unit", MouldingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<PackagingUnitScreenHandler>> PACKAGING_UNIT_SCREEN =
            register("packaging_unit", PackagingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<PlantingUnitScreenHandler>> PLANTING_UNIT_SCREEN =
            register("planting_unit", PlantingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<SeedPickingUnitScreenHandler>> SEED_PICKING_UNIT_SCREEN =
            register("seed_picking_unit", SeedPickingUnitScreenHandler::new);
    public static final RegistryObject<MenuType<ShreddingUnitScreenHandler>> SHREDDING_UNIT_SCREEN =
            register("shredding_unit", ShreddingUnitScreenHandler::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
