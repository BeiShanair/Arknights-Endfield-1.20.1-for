package com.besson.endfield.blockEntity.custom.resourcing;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.BucketFillable;
import com.besson.endfield.blockEntity.custom.logicitis.FluidPipeBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BasePylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BaseRelayBlockEntity;
import com.besson.endfield.screen.custom.resourcing.AcidResistantPumpMkIIScreenHandler;
import com.besson.endfield.util.PumpFluidUtil;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class AcidResistantPumpMkIIBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider, BucketFillable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final int CAPACITY = 50000;
    private static final int POWER_COST = 20 * 20;
    private static final int MAX_STORED_POWER = 10000;

    private final FluidTank fluidTank = new FluidTank(CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final LazyOptional<IFluidHandler> fluidHandlerLazy = LazyOptional.of(() -> fluidTank);

    private int storedPower = 0;
    private boolean isPowered = false;
    private boolean registeredToManager = false;
    private boolean needsInit = true;
    private int powerCheckTimer = 0;
    private int pumpCooldown = 0;

    protected final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> fluidTank.getFluidAmount();
                case 1 -> fluidTank.getCapacity();
                case 2 -> storedPower;
                case 3 -> fluidTank.isEmpty()
                        ? BuiltInRegistries.FLUID.getId(Fluids.WATER)
                        : BuiltInRegistries.FLUID.getId(fluidTank.getFluid().getFluid());
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public AcidResistantPumpMkIIBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACID_RESISTANT_PUMP_MK_II.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AcidResistantPumpMkIIBlockEntity be) {
        if (level.isClientSide()) return;
        
        if (be.needsInit && level instanceof ServerLevel serverLevel) {
            be.needsInit = false;
            PowerNetworkManager.get(serverLevel).registerConsumer(be.getBlockPos(), be::getRequiredPower, be::receiveElectricCharge);
            be.registeredToManager = true;
        }
        
        if (level instanceof ServerLevel serverLevel) {
            be.powerCheckTimer++;
            if (be.powerCheckTimer >= 20) {
                be.powerCheckTimer = 0;
                boolean powered = false;
                Optional<NodeEntry> nearest = PowerNetworkNodeManager.get(serverLevel).findNearest(pos, NodeType.CONSUMER, 10);
                if (nearest.isPresent()) {
                    BlockEntity b = level.getBlockEntity(nearest.get().pos());
                    powered = b instanceof BasePylonBlockEntity || b instanceof BaseRelayBlockEntity;
                }
                be.isPowered = powered;
            }
        }
        
        if (!be.isPowered && be.storedPower < POWER_COST) return;

        Direction facing = be.getCachedFacing();

        be.pumpCooldown++;
        if (be.pumpCooldown < 20) return;
        be.pumpCooldown = 0;
        
        BlockPos waterPos = pos.relative(facing.getOpposite(), 2).below();
        FluidState fluidState = level.getFluidState(waterPos);
        if (!fluidState.is(FluidTags.WATER)) return;
        
        Fluid fluid = fluidState.getType();
        Fluid still = PumpFluidUtil.toStill(fluid);
        int space = CAPACITY - be.fluidTank.getFluidAmount();
        if (space > 0) {
            int toInsert = Math.min(1000, space);
            int inserted = be.fluidTank.fill(new FluidStack(still, toInsert), IFluidHandler.FluidAction.EXECUTE);
            if (inserted > 0 && be.storedPower >= POWER_COST) {
                be.storedPower -= POWER_COST;
            }
        }

        if (be.fluidTank.isEmpty()) return;
        
        BlockPos targetPos = pos.relative(facing);
        BlockEntity targetBe = level.getBlockEntity(targetPos);
        if (!(targetBe instanceof FluidPipeBlockEntity fb)) return;
        if (!fb.canAcceptFrom(facing.getOpposite())) return;

        IFluidHandler target = targetBe
                .getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite())
                .orElse(null);
        if (target == null) return;

        FluidStack toDrain = be.fluidTank.drain(FluidPipeBlockEntity.CAPACITY, IFluidHandler.FluidAction.SIMULATE);
        if (toDrain.isEmpty()) return;

        int accepted = target.fill(toDrain, IFluidHandler.FluidAction.EXECUTE);
        if (accepted > 0) {
            be.fluidTank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
        }
    }
    
    public int getRequiredPower() {
        if (isPowered && storedPower < MAX_STORED_POWER) {
            return POWER_COST;
        }
        return 0;
    }
    
    public void receiveElectricCharge(int amount) {
        this.storedPower = Math.min(this.storedPower + amount * 20, MAX_STORED_POWER);
    }

    public int getStoredPower() {
        return storedPower;
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
    public @NotNull Component getDisplayName() {
        return Component.translatable("blockEntity.acid_resistant_pump_mk_ii");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new AcidResistantPumpMkIIScreenHandler(syncId, playerInventory, this, this.containerData);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == null) return super.getCapability(cap, side);
            if (side == getCachedFacing()) {
                return fluidHandlerLazy.cast();
            }
            return super.getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidHandlerLazy.invalidate();
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(3, 5, 3);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Tank", fluidTank.writeToNBT(new CompoundTag()));
        tag.putInt("storedPower", this.storedPower);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Tank")) {
            fluidTank.readFromNBT(tag.getCompound("Tank"));
        }
        this.storedPower = tag.getInt("storedPower");
    }

    private Direction getCachedFacing() {
        return this.getBlockState().getValue(ModBlockEntityWithFacing.FACING);
    }
    
    @Override
    public void tryFillBucket(Player player) {
        ItemStack cursor = player.containerMenu.getCarried();
        if (cursor.isEmpty() || cursor.getItem() != Items.BUCKET) return;

        FluidStack stored = fluidTank.getFluid();
        if (stored.isEmpty() || stored.getAmount() < 1000) return;

        ItemStack filled = PumpFluidUtil.getFilledBucket(stored.getFluid());
        if (filled == null || filled.isEmpty()) return;

        FluidStack drained = fluidTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
        if (drained.getAmount() != 1000) return;

        cursor.shrink(1);
        if (cursor.isEmpty()) {
            player.containerMenu.setCarried(filled);
        } else if (!player.getInventory().add(filled)) {
            player.drop(filled, false);
        }
    }
}
