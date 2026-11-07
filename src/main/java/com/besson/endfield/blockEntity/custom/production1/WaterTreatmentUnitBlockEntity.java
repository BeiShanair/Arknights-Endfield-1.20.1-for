package com.besson.endfield.blockEntity.custom.production1;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.powering.BasePylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BaseRelayBlockEntity;
import com.besson.endfield.screen.custom.production1.WaterTreatmentUnitScreenHandler;
import com.besson.endfield.util.power.NodeEntry;
import com.besson.endfield.util.power.NodeType;
import com.besson.endfield.util.power.PowerNetworkManager;
import com.besson.endfield.util.power.PowerNetworkNodeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class WaterTreatmentUnitBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final int POWER_PER_TICK = 50;
    public static final int CYCLE_TICKS = 40;
    public static final int PROCESS_AMOUNT = 1000;
    public static final int FLUID_CAPACITY = 50000;

    private static final int MAX_STORED_POWER = 10000;
    private final FluidTank fluidTank = new FluidTank(FLUID_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.of(() -> fluidTank);

    private int tickNum = 0;
    private boolean isPowered = false;
    private int storedPower = 0;
    private boolean isWorking = false;
    private boolean enable = true;
    private int progress = 0;
    private boolean needsInit = true;
    private boolean registeredToManager = false;

    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> WaterTreatmentUnitBlockEntity.this.progress;
                case 1 -> CYCLE_TICKS;
                case 2 -> WaterTreatmentUnitBlockEntity.this.enable ? 1 : 0;
                case 3 -> WaterTreatmentUnitBlockEntity.this.fluidTank.getFluidAmount();
                case 4 -> FLUID_CAPACITY;
                case 5 -> WaterTreatmentUnitBlockEntity.this.storedPower;
                case 6 -> WaterTreatmentUnitBlockEntity.this.isWorking ? 1 : 0;
                case 7 -> WaterTreatmentUnitBlockEntity.this.fluidTank.isEmpty()
                        ? BuiltInRegistries.FLUID.getId(Fluids.WATER)
                        : BuiltInRegistries.FLUID.getId(WaterTreatmentUnitBlockEntity.this.fluidTank.getFluid().getFluid());
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> WaterTreatmentUnitBlockEntity.this.progress = value;
                case 2 -> WaterTreatmentUnitBlockEntity.this.enable = value == 1;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 8;
        }
    };

    public WaterTreatmentUnitBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WATER_TREATMENT_UNIT.get(), pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, WaterTreatmentUnitBlockEntity be) {
        if (world.isClientSide()) return;

        if (be.needsInit && world instanceof ServerLevel serverLevel) {
            be.needsInit = false;
            PowerNetworkManager.get(serverLevel).registerConsumer(pos, be::getRequiredPower, be::receiveElectricCharge);
            be.registeredToManager = true;
        }

        if (!be.enable) {
            be.isWorking = false;
            world.sendBlockUpdated(pos, state, state, 3);
            be.setChanged();
            return;
        }

        be.tickNum++;
        
        if (be.tickNum % 20 == 0 && world instanceof ServerLevel serverLevel) {
            boolean powered = false;
            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverLevel);
            Optional<NodeEntry> nearest = manager.findNearest(pos, NodeType.CONSUMER, 10);
            if (nearest.isPresent()) {
                BlockEntity b = world.getBlockEntity(nearest.get().pos());
                powered = b instanceof BasePylonBlockEntity || b instanceof BaseRelayBlockEntity;
            }
            be.isPowered = powered;
            if (!powered) {
                be.isWorking = false;
            }
            be.tickNum = 0;
        }
        
        if (be.fluidTank.isEmpty()) {
            be.isWorking = false;
            be.progress = 0;
            world.sendBlockUpdated(pos, state, state, 3);
            be.setChanged();
            return;
        }
        
        if (!be.isPowered && be.storedPower < POWER_PER_TICK) {
            be.isWorking = false;
            be.setChanged();
            return;
        }
        
        if (be.storedPower >= POWER_PER_TICK) {
            be.isWorking = true;
            be.storedPower -= POWER_PER_TICK;
            be.progress++;

            if (be.progress >= CYCLE_TICKS) {
                be.processFluid();
                be.progress = 0;
            }
        } else {
            be.isWorking = false;
            be.progress = 0;
        }

        be.setChanged();
        world.sendBlockUpdated(pos, state, state, 3);
    }
    
    private void processFluid() {
        if (fluidTank.isEmpty()) return;
        fluidTank.drain(PROCESS_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
    }

    public void receiveElectricCharge(int amount) {
        this.storedPower = Math.min(this.storedPower + amount * 20, MAX_STORED_POWER);
    }
    
    public int getRequiredPower() {
        if (isPowered && storedPower < MAX_STORED_POWER) {
            return POWER_PER_TICK;
        }
        return 0;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        setChanged();
        if (level != null) {
            BlockPos pos = getBlockPos();
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        }
    }

    public boolean getEnable() {
        return this.enable;
    }

    public boolean isWorking() {
        return isWorking;
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side != null) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> fluidTank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(2, 4, 2);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level instanceof ServerLevel) {
            needsInit = true;
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel && registeredToManager) {
            PowerNetworkManager.get(serverLevel).unregisterConsumer(this.getBlockPos());
            registeredToManager = false;
        }
        super.setRemoved();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("blockEntity.water_treatment_unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new WaterTreatmentUnitScreenHandler(syncId, playerInventory, this, this.containerData);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("FluidTank", fluidTank.writeToNBT(new CompoundTag()));
        tag.putInt("progress", this.progress);
        tag.putInt("storedPower", this.storedPower);
        tag.putBoolean("isWorking", this.isWorking);
        tag.putBoolean("enable", this.enable);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("FluidTank")) {
            fluidTank.readFromNBT(tag.getCompound("FluidTank"));
        }
        this.progress = tag.getInt("progress");
        this.storedPower = tag.getInt("storedPower");
        this.isWorking = tag.getBoolean("isWorking");
        this.enable = tag.getBoolean("enable");
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
