package com.besson.endfield.blockEntity.custom.powering;

import com.besson.endfield.block.ElectrifiableDevice;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.util.PowerNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FEConverterBlockEntity extends BlockEntity {
    private static final int CAPACITY = 10000;
    private static final int CONVERSION_RATE = 325;
    private static final int POWER_PER_TICK = 50;

    private int storedEnergy = 0;
    private final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergyHandler);

    protected boolean needsInit = true;
    protected boolean registeredToManager = false;
    protected int tickNum = 0;

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

    public static void tick(Level level, BlockPos pos, BlockState state, FEConverterBlockEntity be) {
        if (level.isClientSide()) return;

        if (be.needsInit && level instanceof ServerLevel serverWorld) {
            be.needsInit = false;

            PowerNetworkManager.get(serverWorld).registerConsumer(be.getBlockPos(), be::getRequiredPower, be::receiveElectricCharge);
            be.registeredToManager = true;
        }
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        if (pLevel instanceof ServerLevel) {
            needsInit = true;
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            PowerNetworkManager.get(serverLevel).unregisterConsumer(this.getBlockPos());
        }
        super.setRemoved();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void receiveElectricCharge(int amount) {
        storedEnergy = Math.min(storedEnergy + amount, CAPACITY);
        setChanged();
    }


    public boolean needsPower() {
        return storedEnergy < CAPACITY;
    }

    public int getRequiredPower() {
        if (storedEnergy < CAPACITY) {
            return POWER_PER_TICK;
        }
        return 0;
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
