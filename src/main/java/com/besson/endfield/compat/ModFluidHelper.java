package com.besson.endfield.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModFluidHelper {

    private ModFluidHelper() {
    }
    
    public static FluidStack stack(ResourceLocation id, int amount) {
        if (id == null || amount <= 0) {
            return FluidStack.EMPTY;
        }
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
        if (fluid == null || fluid.isSame(Fluids.EMPTY)) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(fluid, amount);
    }
}
