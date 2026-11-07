package com.besson.endfield.fluid;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<FlowingFluid> STILL_SEWAGE = FLUIDS.register("sewage",
            SewageFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_SEWAGE = FLUIDS.register("flowing_sewage",
            SewageFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties SEWAGE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.SEWAGE_FLUID_TYPE, STILL_SEWAGE, FLOWING_SEWAGE)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(ModBlocks.SEWAGE_BLOCK)
            .bucket(ModItems.SEWAGE_BUCKET);

    public static final RegistryObject<FlowingFluid> STILL_PRECIPITATION_ACID = FLUIDS.register("precipitation_acid",
            PrecipitationAcidFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_PRECIPITATION_ACID = FLUIDS.register("flowing_precipitation_acid",
            PrecipitationAcidFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties PRECIPITATION_ACID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PRECIPITATION_ACID_FLUID_TYPE, STILL_PRECIPITATION_ACID, FLOWING_PRECIPITATION_ACID)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(ModBlocks.PRECIPITATION_ACID_BLOCK)
            .bucket(ModItems.PRECIPITATION_ACID_BUCKET);

    public static final RegistryObject<FlowingFluid> STILL_XIRCON_EFFLUENT = FLUIDS.register("xircon_effluent",
            XirconEffluentFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_XIRCON_EFFLUENT = FLUIDS.register("flowing_xircon_effluent",
            XirconEffluentFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties XIRCON_EFFLUENT_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.XIRCON_EFFLUENT_FLUID_TYPE, STILL_XIRCON_EFFLUENT, FLOWING_XIRCON_EFFLUENT)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(ModBlocks.XIRCON_EFFLUENT_BLOCK)
            .bucket(ModItems.XIRCON_EFFLUENT_BUCKET);

    public static final RegistryObject<FlowingFluid> STILL_INERT_XIRCON_EFFLUENT = FLUIDS.register("inert_xircon_effluent",
            InertXirconEffluentFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_INERT_XIRCON_EFFLUENT = FLUIDS.register("flowing_inert_xircon_effluent",
            InertXirconEffluentFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties INERT_XIRCON_EFFLUENT_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.INERT_XIRCON_EFFLUENT_FLUID_TYPE, STILL_INERT_XIRCON_EFFLUENT, FLOWING_INERT_XIRCON_EFFLUENT)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(ModBlocks.INERT_XIRCON_EFFLUENT_BLOCK)
            .bucket(ModItems.INERT_XIRCON_EFFLUENT_BUCKET);

    public static final RegistryObject<FlowingFluid> STILL_YAZHEN_SOLUTION = FLUIDS.register("yazhen_solution",
            YazhenSolutionFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_YAZHEN_SOLUTION = FLUIDS.register("flowing_yazhen_solution",
            YazhenSolutionFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties YAZHEN_SOLUTION_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.YAZHEN_SOLUTION_FLUID_TYPE, STILL_YAZHEN_SOLUTION, FLOWING_YAZHEN_SOLUTION)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(ModBlocks.YAZHEN_SOLUTION_BLOCK)
            .bucket(ModItems.YAZHEN_SOLUTION_BUCKET);

    public static final RegistryObject<FlowingFluid> STILL_JINCAO_SOLUTION = FLUIDS.register("jincao_solution",
            JincaoSolutionFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_JINCAO_SOLUTION = FLUIDS.register("flowing_jincao_solution",
            JincaoSolutionFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties JINCAO_SOLUTION_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.JINCAO_SOLUTION_FLUID_TYPE, STILL_JINCAO_SOLUTION, FLOWING_JINCAO_SOLUTION)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(ModBlocks.JINCAO_SOLUTION_BLOCK)
            .bucket(ModItems.JINCAO_SOLUTION_BUCKET);

    public static final RegistryObject<FlowingFluid> STILL_LIQUID_XIRANITE = FLUIDS.register("liquid_xiranite",
            LiquidXiraniteFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_LIQUID_XIRANITE = FLUIDS.register("flowing_liquid_xiranite",
            LiquidXiraniteFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties LIQUID_XIRANITE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.LIQUID_XIRANITE_FLUID_TYPE, STILL_LIQUID_XIRANITE, FLOWING_LIQUID_XIRANITE)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(ModBlocks.LIQUID_XIRANITE_BLOCK)
            .bucket(ModItems.LIQUID_XIRANITE_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
