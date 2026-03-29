package com.besson.endfield.blockEntity.custom;


import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.RelayTowerBlockEntity;
import com.besson.endfield.util.NodeType;
import com.besson.endfield.util.PowerNetworkManager;
import com.besson.endfield.util.PowerNetworkNodeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BaseIOBlockEntity<R extends Recipe<?>> extends BlockEntity implements MenuProvider {
    protected int tickNum = 0;
    protected boolean isPowered = false;
    protected boolean registeredToManager = false;
    protected int storedPower;
    protected static final int MAX_STORED_POWER = 10000;
    protected boolean isWorking;
    protected boolean enable = true;
    protected int progress = 0;
    protected int maxProgress;

    protected final ContainerData propertyDelegate;
    protected boolean needsInit = true;

    protected final ItemStackHandler itemStackHandler = new ItemStackHandler(getInvSize()) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected LazyOptional<IItemHandler> input = LazyOptional.empty();
    protected LazyOptional<IItemHandler> output = LazyOptional.empty();

    public BaseIOBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int maxProgress) {
        super(type, pos, state);

        this.maxProgress = maxProgress;
        this.propertyDelegate = createPropertyDelegate();

    }
    
    protected abstract int getInvSize();
    protected abstract ContainerData createPropertyDelegate();
    protected abstract int getPowerCostPerTick();
    protected abstract Optional<R> getMatchRecipe(Level world);
    protected abstract void craftItem(Level world);
    protected abstract boolean hasCorrectRecipe(Level world);

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        input.invalidate();
        output.invalidate();
    }
    
    public static <T extends BaseIOBlockEntity<?>> void tick(Level world, BlockPos pos, BlockState state, T be) {
        if (world.isClientSide()) return;
        
        if (be.needsInit && world instanceof ServerLevel serverWorld) {
            be.needsInit = false;

            PowerNetworkManager.get(serverWorld).registerConsumer(be.getBlockPos(), be::getRequiredPower, be::receiveElectricCharge);
            be.registeredToManager = true;
        }

        if (!be.getEnable()) {
            be.isWorking = false;
            world.sendBlockUpdated(pos, state, state, 3);
            be.setChanged();
            return;
        }

        be.tickNum++;

        if (be.tickNum % 20 == 0 && world instanceof ServerLevel serverWorld) {
            AtomicReference<BlockPos> t = new AtomicReference<>();
            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverWorld);
            manager.findNearest(pos, NodeType.CONSUMER, 10).ifPresent(target -> t.set(target.pos()));
            if (t.get() != null) {
                BlockEntity b = world.getBlockEntity(t.get());
                if (b instanceof ElectricPylonBlockEntity || b instanceof RelayTowerBlockEntity) {
                    be.isPowered = true;
                } else {
                    be.isPowered = false;
                    be.isWorking = false;
                    be.setChanged();
                    world.sendBlockUpdated(pos, state, state, 3);
                }
            }
            be.tickNum = 0;
        }

        if (!be.isPowered && be.storedPower < be.getPowerCostPerTick()) return;

        if (be.isOutputSlotAvailable()) {
            boolean hasRecipe = be.hasCorrectRecipe(world);
            if (be.needsPower() || !hasRecipe) {
                be.isWorking = false;
            } else if (!be.needsPower() && !be.isWorking) {
                be.isWorking = true;
            }
            be.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);

            if (hasRecipe && be.storedPower >= be.getPowerCostPerTick()) {
                be.incrementProgress();
                be.storedPower -= be.getPowerCostPerTick();
                if (be.hasCraftingFinished()) {
                    be.craftItem(world);
                    be.resetProgress();
                }
            } else {
                be.resetProgress();
            }
        } else {
            be.resetProgress();
        }
        be.setChanged();
    }

    protected boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    public NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(itemStackHandler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            items.set(i, itemStackHandler.getStackInSlot(i));
        }
        return items;
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
    
    public void receiveElectricCharge(int amount) {
        this.storedPower = Math.min(this.storedPower + amount * 20, MAX_STORED_POWER);
    }

    public boolean needsPower() {
        return this.storedPower < getPowerCostPerTick();
    }

    public int getRequiredPower() {
        if (isWorking || isPowered && storedPower < MAX_STORED_POWER) {
            return getPowerCostPerTick();
        }
        return 0;
    }

    // region 数据持久化 & 同步
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("progress");
        this.storedPower = pTag.getInt("storedPower");
        this.isWorking = pTag.getBoolean("isWorking");
        this.enable = pTag.getBoolean("enable");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", itemStackHandler.serializeNBT());
        pTag.putInt("progress", this.progress);
        pTag.putInt("storedPower", this.storedPower);
        pTag.putBoolean("isWorking", this.isWorking);
        pTag.putBoolean("enable", this.enable);
    }
    
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }
    // endregion

    protected void resetProgress() {
        this.progress = 0;
    }

    protected void incrementProgress() {
        this.progress++;
    }

    protected boolean canOutputAccept(ItemStack result) {
        ItemStack out = itemStackHandler.getStackInSlot(getOutputSlotIndex());
        return (out.isEmpty() || out.getItem() == result.getItem())
                && out.getCount() + result.getCount() <= out.getMaxStackSize();
    }

    protected boolean isOutputSlotAvailable() {
        ItemStack outputStack = itemStackHandler.getStackInSlot(getOutputSlotIndex());
        return outputStack.isEmpty() || outputStack.getCount() < outputStack.getMaxStackSize();
    }
    
    protected abstract int getOutputSlotIndex();
}
