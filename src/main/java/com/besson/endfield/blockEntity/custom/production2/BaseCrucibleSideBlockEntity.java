package com.besson.endfield.blockEntity.custom.production2;

import com.besson.endfield.blockEntity.custom.BaseIOSideBlockEntity;
import com.besson.endfield.blockEntity.custom.logicitis.BeltBlockEntity;
import com.besson.endfield.blockEntity.custom.logicitis.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public abstract class BaseCrucibleSideBlockEntity extends BaseIOSideBlockEntity {
    protected int fluidPortIndex = -1;
    protected int particleTick = 0;

    public BaseCrucibleSideBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public abstract @Nullable BaseCrucibleBlockEntity getParentBlock();

    public int getFluidPortIndex() {
        return fluidPortIndex;
    }
    
    public void cycleFluidPort() {
        this.fluidPortIndex = this.fluidPortIndex >= 1 ? -1 : this.fluidPortIndex + 1;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            if (fluidPortIndex >= 0 && level instanceof ServerLevel serverLevel) {
                spawnPortMarker(serverLevel, worldPosition, fluidPortIndex, 12);
            }
        }
    }
    
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        BaseCrucibleBlockEntity parent = getParentBlock();
        if (parent == null || level == null || side == null) {
            return super.getCapability(cap, side);
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER
                && level.getBlockEntity(worldPosition.relative(side)) instanceof FluidPipeBlockEntity) {
            return parent.getPortFluidHandler(fluidPortIndex).cast();
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER
                && level.getBlockEntity(worldPosition.relative(side)) instanceof BeltBlockEntity) {
            return parent.getSharedItemHandler().cast();
        }

        return super.getCapability(cap, side);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BaseCrucibleSideBlockEntity be) {
        BaseIOSideBlockEntity.tick(level, pos, state, be);

        if (!(level instanceof ServerLevel serverLevel)) return;
        if (be.fluidPortIndex < 0) return;
        if (++be.particleTick < 20) return;
        be.particleTick = 0;
        spawnPortMarker(serverLevel, pos, be.fluidPortIndex, 3);
    }
    
    public static void spawnPortMarker(ServerLevel level, BlockPos pos, int portIndex, int count) {
        Vector3f color = portIndex == 0
                ? new Vector3f(0.2f, 0.85f, 1.0f)
                : new Vector3f(1.0f, 0.6f, 0.15f);
        level.sendParticles(new DustParticleOptions(color, 1.2f),
                pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
                count, 0.35, 0.05, 0.35, 0.01);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("fluidPort", fluidPortIndex);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        fluidPortIndex = tag.contains("fluidPort") ? tag.getInt("fluidPort") : -1;
    }
}
