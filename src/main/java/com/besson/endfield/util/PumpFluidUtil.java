package com.besson.endfield.util;

import com.besson.endfield.fluid.ModFluids;
import com.besson.endfield.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public final class PumpFluidUtil {
    private PumpFluidUtil() {
    }

    private static Fluid stillOf(@Nullable Fluid fluid) {
        if (fluid instanceof FlowingFluid flowing) return flowing.getSource();
        return fluid;
    }
    
    public static boolean isAcidic(@Nullable Fluid fluid) {
        if (fluid == null) return false;
        return stillOf(fluid) == ModFluids.STILL_PRECIPITATION_ACID.get();
    }

    public static Fluid toStill(@Nullable Fluid fluid) {
        if (fluid == null) return Fluids.EMPTY;
        return stillOf(fluid);
    }

    @Nullable
    public static ItemStack getFilledBucket(@Nullable Fluid fluid) {
        if (fluid == null) return null;
        Fluid still = stillOf(fluid);

        if (still == Fluids.WATER) return new ItemStack(Items.WATER_BUCKET);
        if (still == Fluids.LAVA) return new ItemStack(Items.LAVA_BUCKET);

        if (still == ModFluids.STILL_SEWAGE.get()) return new ItemStack(ModItems.SEWAGE_BUCKET.get());
        if (still == ModFluids.STILL_PRECIPITATION_ACID.get()) return new ItemStack(ModItems.PRECIPITATION_ACID_BUCKET.get());
        if (still == ModFluids.STILL_XIRCON_EFFLUENT.get()) return new ItemStack(ModItems.XIRCON_EFFLUENT_BUCKET.get());
        if (still == ModFluids.STILL_INERT_XIRCON_EFFLUENT.get()) return new ItemStack(ModItems.INERT_XIRCON_EFFLUENT_BUCKET.get());
        if (still == ModFluids.STILL_YAZHEN_SOLUTION.get()) return new ItemStack(ModItems.YAZHEN_SOLUTION_BUCKET.get());
        if (still == ModFluids.STILL_JINCAO_SOLUTION.get()) return new ItemStack(ModItems.JINCAO_SOLUTION_BUCKET.get());
        if (still == ModFluids.STILL_LIQUID_XIRANITE.get()) return new ItemStack(ModItems.LIQUID_XIRANITE_BUCKET.get());

        return null;
    }
}
