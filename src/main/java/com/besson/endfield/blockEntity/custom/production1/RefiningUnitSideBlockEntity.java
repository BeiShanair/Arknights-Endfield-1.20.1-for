package com.besson.endfield.blockEntity.custom.production1;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.BaseIOSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import static com.besson.endfield.block.ModBlockEntityWithFacing.FACING;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RefiningUnitSideBlockEntity extends BaseIOSideBlockEntity {
    public RefiningUnitSideBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFINING_UNIT_SIDE.get(), pos, state);
    }

    public @Nullable RefiningUnitBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity entity = level.getBlockEntity(parentPos);
        if (entity instanceof RefiningUnitBlockEntity entity1) {
            return entity1;
        }
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            RefiningUnitBlockEntity parent  = getParentBlock();
            if (parent != null) {
                return parent.getCapability(cap, side);
            }
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            RefiningUnitBlockEntity parent  = getParentBlock();
            if (parent != null && parent.nowFluidMode) {
                return parent.getFluidInterface().cast();
            }
        }
        return super.getCapability(cap, side);
    }

    public static <T extends BaseIOSideBlockEntity> void tick(Level world, BlockPos pos, BlockState state, T be) {
        BaseIOSideBlockEntity.tick(world, pos, state, be);

        if (world.isClientSide()) return;
        RefiningUnitBlockEntity parent = (RefiningUnitBlockEntity) be.getParentBlock();
        if (parent == null) return;
        if (parent.sewageTank.getFluidAmount() <= 0) return;

        Direction facing = state.getValue(FACING);
        Direction dir = facing.getCounterClockWise();
        BlockPos targetPos = pos.relative(dir);

        BlockEntity targetBe = world.getBlockEntity(targetPos);
        if (targetBe == null) return;

        LazyOptional<IFluidHandler> cap = targetBe.getCapability(ForgeCapabilities.FLUID_HANDLER, dir);
        if (!cap.isPresent()) return;

        IFluidHandler handler = cap.orElse(null);
        if (handler == null) return;

        FluidStack current = parent.sewageTank.getFluid();
        if (current.isEmpty()) return;

        int toTransfer = Math.min(2000, current.getAmount());
        FluidStack sim = current.copy();
        sim.setAmount(toTransfer);

        int accepted = handler.fill(sim, FluidAction.SIMULATE);
        if (accepted <= 0) return;

        FluidStack drained = parent.sewageTank.drain(accepted, FluidAction.EXECUTE);
        if (drained.isEmpty()) return;

        int filled = handler.fill(drained, FluidAction.EXECUTE);
        if (filled < drained.getAmount()) {
            int remainder = drained.getAmount() - filled;
            parent.sewageTank.fill(new FluidStack(drained.getFluid(), remainder), FluidAction.EXECUTE);
        }
    }
}
