package com.besson.endfield.blockEntity.custom.depot;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.item.custom.FluidBottleItem;
import com.besson.endfield.item.custom.MaterialBottleItem;
import com.besson.endfield.screen.custom.resourcing.FluidTankScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

public class FluidTankBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final int CAPACITY = 500000;

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

    protected final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> fluidTank.getFluidAmount();
                case 1 -> fluidTank.getCapacity();
                case 2 -> BuiltInRegistries.FLUID.getId(fluidTank.getFluid().getFluid());
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public FluidTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_TANK.get(), pos, state);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("blockEntity.fluid_tank");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FluidTankScreenHandler(syncId, playerInventory, this, this.containerData);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandlerLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidHandlerLazy.invalidate();
    }

    /** 模型约 3.5 格高、横向也超出单格，渲染包围盒要放大，否则站在边缘看会整块消失。 */
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
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Tank")) {
            fluidTank.readFromNBT(tag.getCompound("Tank"));
        }
    }
    
    public static final int BOTTLE_TRANSFER_AMOUNT = 1000;
    public void tryTransferWithBottle(Player player) {
        if (!(player.containerMenu instanceof FluidTankScreenHandler handler) || handler.getBlockEntity() != this) {
            return;
        }
        ItemStack cursor = player.containerMenu.getCarried();
        if (cursor.isEmpty()) return;

        ItemStack result;
        if (cursor.getItem() instanceof FluidBottleItem bottle) {
            FluidStack request = new FluidStack(bottle.getBottledFluid(), BOTTLE_TRANSFER_AMOUNT);
            if (request.isEmpty()) return;
            if (fluidTank.fill(request, IFluidHandler.FluidAction.SIMULATE) < BOTTLE_TRANSFER_AMOUNT) return;
            fluidTank.fill(request, IFluidHandler.FluidAction.EXECUTE);
            result = new ItemStack(bottle.getEmptyBottle());
            if (level != null) {
                level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        } else if (cursor.getItem() instanceof MaterialBottleItem materialBottle) {
            FluidStack stored = fluidTank.getFluid();
            if (stored.isEmpty()) return;
            Item bottled = materialBottle.getBottledItem(stored.getFluid());
            if (bottled == null) return;
            if (fluidTank.drain(BOTTLE_TRANSFER_AMOUNT, IFluidHandler.FluidAction.SIMULATE).getAmount() < BOTTLE_TRANSFER_AMOUNT) {
                return;
            }
            fluidTank.drain(BOTTLE_TRANSFER_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
            result = new ItemStack(bottled);
            if (level != null) {
                level.playSound(null, worldPosition, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        } else {
            return;
        }

        cursor.shrink(1);
        if (cursor.isEmpty()) {
            player.containerMenu.setCarried(result);
        } else if (!player.getInventory().add(result)) {
            player.drop(result, false);
        }
    }
}
