package com.besson.endfield.blockEntity.custom.powering;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.util.power.NodeEntry;
import com.besson.endfield.util.power.NodeType;
import com.besson.endfield.util.power.PowerNetworkManager;
import com.besson.endfield.screen.custom.powering.ProtocolAnchorCoreScreenHandler;
import com.besson.endfield.util.power.PowerNetworkNodeManager;
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

    private boolean needsInit = true;
    private boolean registeredToManager = false;
    private BlockPos[] forcedChunks = null;

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

    public static void tick(Level world, BlockPos pos, BlockState state, ProtocolAnchorCoreBlockEntity be) {
        if (world.isClientSide()) return;

        if (be.needsInit && world instanceof ServerLevel serverWorld) {
            be.needsInit = false;

            PowerNetworkManager.get(serverWorld).registerGenerator(pos, () -> 200);

            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverWorld);
            manager.register(new NodeEntry(pos, NodeType.CORE));
            be.registeredToManager = true;
            be.setForceLoadedChunks(serverWorld, pos);
        }
        
        be.setChanged();
    }

    private void setForceLoadedChunks(ServerLevel serverWorld, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        forcedChunks = new BlockPos[9];
        int index = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int targetChunkX = chunkX + dx;
                int targetChunkZ = chunkZ + dz;
                serverWorld.setChunkForced(targetChunkX, targetChunkZ, true);
                forcedChunks[index++] = new BlockPos(targetChunkX, 0, targetChunkZ);
            }
        }
    }

    private void clearForceLoadedChunks(ServerLevel serverWorld) {
        if (forcedChunks != null) {
            for (BlockPos chunkPos : forcedChunks) {
                if (chunkPos != null) {
                    serverWorld.setChunkForced(chunkPos.getX(), chunkPos.getZ(), false);
                }
            }
            forcedChunks = null;
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
        if (pLevel instanceof ServerLevel) {
            needsInit = true;
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            PowerNetworkManager.get(serverLevel).unregisterGenerator(this.getBlockPos());
            PowerNetworkNodeManager.get(serverLevel).unregister(this.getBlockPos());
            registeredToManager = false;
            clearForceLoadedChunks(serverLevel);
        }
        super.setRemoved();
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
        pTag.put("inventory", sideHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
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

    public IItemHandler getItemHandler() {
        return sideHandler;
    }
}
