package com.besson.endfield.blockEntity.custom.powering;

import com.besson.endfield.block.custom.powering.ThermalBankBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.item.ModItems;
import com.besson.endfield.util.PowerNetworkManager;
import com.besson.endfield.screen.custom.ThermalBankScreenHandler;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ThermalBankBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int burnTime;
    private int fuelTime;
    protected final ContainerData propertyDelegate;
    private boolean registeredToManager = false;
    protected boolean isWorking;
    protected boolean enable = true;
    protected boolean needsInit = true;

    public static final int INPUT_SLOT = 0;
    private Item burnItem = null;

    private final LazyOptional<ItemStackHandler> lazyOptional = LazyOptional.of(() -> itemStackHandler);

    public ThermalBankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.THERMAL_BANK.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ThermalBankBlockEntity.this.burnTime;
                    case 1 -> ThermalBankBlockEntity.this.fuelTime;
                    case 2 -> ThermalBankBlockEntity.this.enable ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ThermalBankBlockEntity.this.burnTime = value;
                    case 1 -> ThermalBankBlockEntity.this.fuelTime = value;
                    case 2 -> ThermalBankBlockEntity.this.enable = value == 1;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    public NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
        list.set(0, itemStackHandler.getStackInSlot(0));
        return list;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ThermalBankBlockEntity entity) {
        if (world.isClientSide()) return;

        if (entity.needsInit && world instanceof ServerLevel serverWorld) {
            entity.needsInit = false;

            PowerNetworkManager.get(serverWorld).registerGenerator(entity.getBlockPos(), entity::getPowerOutput);
            entity.registeredToManager = true;
        }

        if (!entity.getEnable()) {
            entity.isWorking = false;
            world.sendBlockUpdated(pos, state, state, 3);
            entity.setChanged();
            return;
        }
        
        if (entity.burnTime > 0) {
            entity.burnTime--;
        }

        if (entity.burnTime == 0 && !entity.itemStackHandler.getStackInSlot(0).isEmpty()) {
            entity.burnItem = null;
            ItemStack stack = entity.itemStackHandler.getStackInSlot(INPUT_SLOT);
            Integer fuelValue;

            if (stack.is(ModItems.ORIGINIUM_ORE.get())) {
                fuelValue = 160;
            } else if (stack.is(ModItems.LC_BATTERY.get()) ||
                    stack.is(ModItems.SC_BATTERY.get()) ||
                    stack.is(ModItems.HC_BATTERY.get())) {
                fuelValue = 800;
            } else {
                fuelValue = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
            }
            entity.burnItem = stack.getItem();
            if (fuelValue != null && fuelValue > 0) {
                int fuelTime = fuelValue / 2;
                entity.fuelTime = fuelTime;
                entity.burnTime = fuelTime;

                if (stack.is(Items.LAVA_BUCKET)) {
                    stack = new ItemStack(Items.BUCKET);
                } else {
                    stack.shrink(1);
                }
                entity.itemStackHandler.setStackInSlot(INPUT_SLOT, stack);
                entity.setChanged();
            }
        }
    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    public int getPowerOutput() {
        if (!enable || !isBurning()) return 0;
        if (burnItem == ModItems.ORIGINIUM_ORE.get()) {
            return 50;
        } else if (burnItem == ModItems.LC_BATTERY.get()) {
            return 220;
        } else if (burnItem == ModItems.SC_BATTERY.get()) {
            return 420;
        } else if (burnItem == ModItems.HC_BATTERY.get()) {
            return 1100;
        }
        return 50;
    }

    public float getFuelProgress() {
        if (fuelTime == 0) return 0;
        return (float) burnTime / (float) fuelTime;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        itemStackHandler.serializeNBT();
        pTag.putInt("burnTime", burnTime);
        pTag.putInt("fuelTime", fuelTime);
        pTag.putBoolean("isWorking", this.isWorking);
        pTag.putBoolean("enable", this.enable);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.burnTime = pTag.getInt("burnTime");
        this.fuelTime = pTag.getInt("fuelTime");
        this.isWorking = pTag.getBoolean("isWorking");
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
        return Component.translatable("blockEntity.thermal_bank");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ThermalBankScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 4, 0);
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
            PowerNetworkManager.get(serverLevel).unregisterGenerator(this.getBlockPos());
            registeredToManager = false;
        }
        super.setRemoved();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyOptional.invalidate();
    }

    public ContainerData getPropertyDelegate() {
        return propertyDelegate;
    }

    protected Direction getFacing(BlockState state) {
        return state.getValue(ThermalBankBlock.FACING);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), level.getBlockState(getBlockPos()), 3);
        }
    }

    public boolean getEnable() {
        return this.enable;
    }
}
