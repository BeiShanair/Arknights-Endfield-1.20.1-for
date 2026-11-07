package com.besson.endfield.blockEntity.custom.resourcing;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.BaseIOSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidPumpSideBlockEntity extends BaseIOSideBlockEntity {

    public FluidPumpSideBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_PUMP_SIDE.get(), pos, state);
    }

    @Override
    public @Nullable FluidPumpBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity entity = level.getBlockEntity(parentPos);
        if (entity instanceof FluidPumpBlockEntity pump) {
            return pump;
        }
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            FluidPumpBlockEntity parent = getParentBlock();
            if (parent != null) {
                // 主机只在正面（facing）暴露流体接口；端口方块不关心管道从哪一面接过来，一律按正面去要能力
                return parent.getCapability(cap, parent.getBlockState().getValue(ModBlockEntityWithFacing.FACING));
            }
        }
        return super.getCapability(cap, side);
    }
}
