package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.custom.ProtocolAnchorCoreBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.power.PowerNetworkManager;
import com.besson.endfield.screen.custom.ProtocolAnchorCoreScreenHandler;
import com.besson.endfield.util.ProtocolAnchorCoreStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ProtocolAnchorCoreBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int buffer = 0;
    private int cachedNearbyPower = 0;

    private boolean registeredToManager = false;

    private final ItemStackHandler sideHandler = new ItemStackHandler(54) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<IItemHandler> sideLazy = LazyOptional.of(() -> sideHandler);

    public ProtocolAnchorCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PROTOCOL_ANCHOR_CORE.get(), pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ProtocolAnchorCoreBlockEntity entity) {
        if (world.isClientSide()) return;
        int totalPower = entity.getTotalPower();
        int newBuffer = Math.min(entity.buffer + totalPower, entity.getMaxBuffer());

        if (newBuffer != entity.buffer) {
            entity.buffer = newBuffer;
            entity.setChanged();
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(2, 27, 2);
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
        if (!registeredToManager && pLevel instanceof ServerLevel serverLevel) {
            PowerNetworkManager.get(serverLevel).registerGenerator(this.getBlockPos(), () -> {
                try {
                    return this.getTotalPower();
                } catch (Throwable t) {
                    return 0;
                }
            });
            registeredToManager = true;
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            PowerNetworkManager.get(serverLevel).unregisterGenerator(this.getBlockPos());
        }
        super.setRemoved();
    }

    private int getNearbyThermalBankPower() {
        int sum = 0;
        BlockPos blockPos = this.getBlockPos();
        if (level != null) {
            for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-30, -30, -30), blockPos.offset(30, 30, 30))) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof ThermalBankBlockEntity blockEntity) {
                    sum += blockEntity.getPowerOutput();
                }
            }
        }
        return sum;
    }

    public void writeScreenData(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
        ServerLevel level = (ServerLevel) this.getLevel();
        PowerNetworkManager manager = PowerNetworkManager.get(level);
        buf.writeDouble(manager.getLastSupplyRatio());
        buf.writeInt(manager.getLastTotalGenerated());
        buf.writeInt(manager.getLastTotalDemand());
        buf.writeInt(manager.getCurrentStoredEnergy());
    }

    public int getMaxBuffer() {
        int baseMaxBuffer = 100000;
        return baseMaxBuffer + getNearbyThermalBankPower();
    }

    public void refreshNearbyPower() {
        this.cachedNearbyPower = getNearbyThermalBankPower();
        setChanged();
    }

    private int getExtraPower() {
        return cachedNearbyPower;
    }

    public int getTotalPower() {
        int basePower = 150;
        return basePower + cachedNearbyPower;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0,
                state -> state.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.protocol_anchor_core");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ProtocolAnchorCoreScreenHandler(pContainerId, pPlayerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("buffer", this.buffer);
        pTag.put("inventory", sideHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.buffer = pTag.getInt("buffer");
        sideHandler.deserializeNBT(pTag.getCompound("inventory"));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    public NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < sideHandler.getSlots(); i++) {
            items.add(sideHandler.getStackInSlot(i));
        }
        return items;
    }
}
