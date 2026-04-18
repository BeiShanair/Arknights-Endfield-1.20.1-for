package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.RelayTowerBlockEntity;
import com.besson.endfield.screen.custom.logicitis.ProtocolStashScreenHandler;
import com.besson.endfield.util.power.NodeType;
import com.besson.endfield.util.power.PowerNetworkManager;
import com.besson.endfield.util.power.PowerNetworkNodeManager;
import com.besson.endfield.util.storage.GlobalStorageManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.concurrent.atomic.AtomicReference;

public class ProtocolStashBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {
    private boolean isPowered = false;
    protected boolean enable = true;
    protected final ContainerData propertyDelegate;
    private boolean registeredToManager = false;
    private int tickNum = 0;
    private boolean needsInit = true;

    private static final int SUBMIT_INTERVAL = 100;
    private int submitTimer = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler inv = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<IItemHandler> sideLazy = LazyOptional.of(() -> inv);

    public ProtocolStashBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PROTOCOL_STASH.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return ProtocolStashBlockEntity.this.enable ? 1 : 0;
            }

            @Override
            public void set(int pIndex, int pValue) {
                ProtocolStashBlockEntity.this.enable = pValue == 1;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ProtocolStashBlockEntity be) {
        if (world.isClientSide()) return;
        
        if (be.needsInit && world instanceof ServerLevel serverWorld) {
            be.needsInit = false;
            PowerNetworkManager.get(serverWorld).registerConsumer(
                be.getBlockPos(), () -> 0, amount -> {});
            be.registeredToManager = true;
        }

        if (!be.getEnable()) {
            world.sendBlockUpdated(pos, state, state, 3);
            be.setChanged();
            return;
        }
        be.tickNum++;

        if (be.tickNum % 20 == 0 && world instanceof ServerLevel serverWorld) {
            AtomicReference<BlockPos> nearestPower = new AtomicReference<>();
            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverWorld);
            manager.findNearest(pos, NodeType.CONSUMER, 10).ifPresent(target -> nearestPower.set(target.pos()));
            
            if (nearestPower.get() != null) {
                BlockEntity nearbyBE = world.getBlockEntity(nearestPower.get());
                be.isPowered = nearbyBE instanceof ElectricPylonBlockEntity || nearbyBE instanceof RelayTowerBlockEntity;
            } else {
                be.isPowered = false;
            }
            be.tickNum = 0;
            be.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);
        }

        if (be.isPowered) {
            be.submitTimer++;
            if (be.submitTimer >= SUBMIT_INTERVAL) {
                be.submitTimer = 0;
                be.flushToGlobalStorage(world);
            }
        }
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return sideLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        sideLazy.invalidate();
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        if (pLevel instanceof ServerLevel) {
            needsInit = true;
        }
    }
    
    public void flushToGlobalStorage(Level world) {
        if (!(world instanceof ServerLevel serverWorld)) return;
        
        GlobalStorageManager manager = GlobalStorageManager.get(serverWorld);
        boolean changed = false;

        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                long inserted = manager.insert(stack);
                if (inserted > 0) {
                    stack.shrink((int) inserted);
                    changed = true;
                }
            }
        }

        if (changed) setChanged();
        
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverWorld) {
            PowerNetworkManager.get(serverWorld).unregisterConsumer(this.getBlockPos());
            registeredToManager = false;
        }
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("isPowered", this.isPowered);
        pTag.putInt("submitTimer", this.submitTimer);
        pTag.put("inventory", inv.serializeNBT());
        pTag.putBoolean("enable", this.enable);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.isPowered = pTag.getBoolean("isPowered");
        this.submitTimer = pTag.getInt("submitTimer");
        inv.deserializeNBT(pTag.getCompound("inventory"));
        this.enable = pTag.getBoolean("enable");
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
    public Component getDisplayName() {
        return Component.translatable("blockEntity.protocol_stash");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ProtocolStashScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    public boolean isPowered() {
        return isPowered;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
    public NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> inv = NonNullList.withSize(27, ItemStack.EMPTY);
        for (int i = 0; i < this.inv.getSlots(); i++) {
            inv.set(i, this.inv.getStackInSlot(i));
        }
        return inv;
    }
}
