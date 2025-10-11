package com.besson.endfield.blockEntity;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.custom.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrafterBlockEntity>> CRAFTER =
            BLOCK_ENTITIES.register("crafter", () -> BlockEntityType.Builder.of(
                    CrafterBlockEntity::new, ModBlocks.CRAFTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<PortableOriginiumRigBlockEntity>> PORTABLE_ORIGINIUM_RIG =
            BLOCK_ENTITIES.register("portable_originium_rig", () -> BlockEntityType.Builder.of(
                    PortableOriginiumRigBlockEntity::new, ModBlocks.PORTABLE_ORIGINIUM_RIG.get()).build(null));
    public static final RegistryObject<BlockEntityType<ProtocolAnchorCoreBlockEntity>> PROTOCOL_ANCHOR_CORE =
            BLOCK_ENTITIES.register("protocol_anchor_core", () -> BlockEntityType.Builder.of(
                    ProtocolAnchorCoreBlockEntity::new, ModBlocks.PROTOCOL_ANCHOR_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ThermalBankBlockEntity>> THERMAL_BANK =
            BLOCK_ENTITIES.register("thermal_bank", () -> BlockEntityType.Builder.of(
                    ThermalBankBlockEntity::new, ModBlocks.THERMAL_BANK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ThermalBankSideBlockEntity>> THERMAL_BANK_SIDE =
            BLOCK_ENTITIES.register("thermal_bank_side", () -> BlockEntityType.Builder.of(
                    ThermalBankSideBlockEntity::new, ModBlocks.THERMAL_BANK_SIDE.get()).build(null));
    public static final RegistryObject<BlockEntityType<RelayTowerBlockEntity>> RELAY_TOWER =
            BLOCK_ENTITIES.register("relay_tower", () -> BlockEntityType.Builder.of(
                    RelayTowerBlockEntity::new, ModBlocks.RELAY_TOWER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ElectricPylonBlockEntity>> ELECTRIC_PYLON =
            BLOCK_ENTITIES.register("electric_pylon", () -> BlockEntityType.Builder.of(
                    ElectricPylonBlockEntity::new, ModBlocks.ELECTRIC_PYLON.get()).build(null));
    public static final RegistryObject<BlockEntityType<ElectricMiningRigBlockEntity>> ELECTRIC_MINING_RIG =
            BLOCK_ENTITIES.register("electric_mining_rig", () -> BlockEntityType.Builder.of(
                    ElectricMiningRigBlockEntity::new, ModBlocks.ELECTRIC_MINING_RIG.get()).build(null));
    public static final RegistryObject<BlockEntityType<ElectricMiningRigMkIIBlockEntity>> ELECTRIC_MINING_RIG_MK_II =
            BLOCK_ENTITIES.register("electric_mining_rig_mk2", () -> BlockEntityType.Builder.of(
                    ElectricMiningRigMkIIBlockEntity::new, ModBlocks.ELECTRIC_MINING_RIG_MK_II.get()).build(null));
    public static final RegistryObject<BlockEntityType<RefiningUnitBlockEntity>> REFINING_UNIT =
            BLOCK_ENTITIES.register("refining_unit", () -> BlockEntityType.Builder.of(
                    RefiningUnitBlockEntity::new, ModBlocks.REFINING_UNIT.get()).build(null));
    public static final RegistryObject<BlockEntityType<RefiningUnitSideBlockEntity>> REFINING_UNIT_SIDE =
            BLOCK_ENTITIES.register("refining_unit_side", () -> BlockEntityType.Builder.of(
                    RefiningUnitSideBlockEntity::new, ModBlocks.REFINING_UNIT_SIDE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
