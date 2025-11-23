package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.ElectrifiableDevice;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FEConverterBlockEntity extends BlockEntity implements ElectrifiableDevice {
    private static final int CAPACITY = 10000;
    private static final int CONVERSION_RATE = 4;
    private static final int POWER_PER_TICK = 20;

    private int storedEnergy = 0;
    private final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergyHandler);

    public FEConverterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FE_CONVERTER.get(), pPos, pBlockState);
    }

    private IEnergyStorage createEnergyHandler() {
        return new IEnergyStorage() {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return 0;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int initialEnergy = Math.min(maxExtract / CONVERSION_RATE, storedEnergy);
                if (!simulate) {
                    storedEnergy -= initialEnergy;
                }
                return initialEnergy * CONVERSION_RATE;
            }

            @Override
            public int getEnergyStored() {
                return storedEnergy * CONVERSION_RATE;
            }

            @Override
            public int getMaxEnergyStored() {
                return CAPACITY * CONVERSION_RATE;
            }

            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public boolean canReceive() {
                return false;
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void receiveElectricCharge(int amount) {
        storedEnergy = Math.min(storedEnergy + amount, CAPACITY);
        setChanged();
    }

    @Override
    public boolean needsPower() {
        return storedEnergy < CAPACITY;
    }

    @Override
    public int getRequiredPower() {
        return POWER_PER_TICK;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyStorage.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("StoredEnergy", storedEnergy);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        storedEnergy = pTag.getInt("StoredEnergy");
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }
}
