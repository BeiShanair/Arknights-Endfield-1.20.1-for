package com.besson.endfield.fluid;

import com.besson.endfield.ArknightsEndfield;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ArknightsEndfield.MOD_ID);

    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.parse("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.parse("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = ResourceLocation.parse("block/water_overlay");

    // 颜色取自 Fabric 端 SimpleFluidRenderHandler 的 tint 值，雾色按同一 RGB 归一化
    public static final RegistryObject<FluidType> SEWAGE_FLUID_TYPE = FLUID_TYPES.register("sewage",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF2F4F4F,
                    new Vector3f(47f / 255f, 79f / 255f, 79f / 255f),
                    FluidType.Properties.create().density(15).viscosity(5)));

    public static final RegistryObject<FluidType> PRECIPITATION_ACID_FLUID_TYPE = FLUID_TYPES.register("precipitation_acid",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFFFFD700,
                    new Vector3f(255f / 255f, 215f / 255f, 0f / 255f),
                    FluidType.Properties.create().density(15).viscosity(5)));

    public static final RegistryObject<FluidType> XIRCON_EFFLUENT_FLUID_TYPE = FLUID_TYPES.register("xircon_effluent",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF557559,
                    new Vector3f(85f / 255f, 117f / 255f, 89f / 255f),
                    FluidType.Properties.create().density(15).viscosity(5)));

    public static final RegistryObject<FluidType> INERT_XIRCON_EFFLUENT_FLUID_TYPE = FLUID_TYPES.register("inert_xircon_effluent",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF194B30,
                    new Vector3f(25f / 255f, 75f / 255f, 48f / 255f),
                    FluidType.Properties.create().density(15).viscosity(5)));

    public static final RegistryObject<FluidType> YAZHEN_SOLUTION_FLUID_TYPE = FLUID_TYPES.register("yazhen_solution",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFFC0FF99,
                    new Vector3f(192f / 255f, 255f / 255f, 153f / 255f),
                    FluidType.Properties.create().density(15).viscosity(5)));

    public static final RegistryObject<FluidType> JINCAO_SOLUTION_FLUID_TYPE = FLUID_TYPES.register("jincao_solution",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFFD7FCD5,
                    new Vector3f(215f / 255f, 252f / 255f, 213f / 255f),
                    FluidType.Properties.create().density(15).viscosity(5)));

    public static final RegistryObject<FluidType> LIQUID_XIRANITE_FLUID_TYPE = FLUID_TYPES.register("liquid_xiranite",
            () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL,
                    0xFF376C0E,
                    new Vector3f(55f / 255f, 108f / 255f, 14f / 255f),
                    FluidType.Properties.create().density(15).viscosity(5)));

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
