package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.screen.custom.logicitis.DepotUnloaderScreenHandler;
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
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DepotUnloaderBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private ItemStack filter = ItemStack.EMPTY;
    private boolean working = false;
    private int tickNum = 0;
    private final SimpleContainer filterInv = new SimpleContainer(1) {
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    public DepotUnloaderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DEPOT_UNLOADER.get(), pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, DepotUnloaderBlockEntity be) {
        if (world.isClientSide()) return;

        be.tickNum++;
        if (be.tickNum % 20 == 0) {
            be.tickNum = 0;
            Direction facing = state.getValue(ModBlockEntityWithFacing.FACING);
            BlockEntity backEn = world.getBlockEntity(pos.relative(facing.getOpposite()));
            be.working = backEn instanceof DepotBusSectionBlockEntity || backEn instanceof DepotBusSectionSideBlockEntity;
            be.setChanged();
        }

        if (world instanceof ServerLevel serverWorld && be.working && !be.filter.isEmpty()) {
            GlobalStorageManager manager = GlobalStorageManager.get(serverWorld);
            Direction fac = state.getValue(ModBlockEntityWithFacing.FACING);
            BlockEntity forwardBe = world.getBlockEntity(pos.relative(fac));

            if (!(forwardBe instanceof BeltBlockEntity belt)) return;

            if (!belt.canAcceptFrom(fac)) return;
            
            belt.acceptItem(manager.extract(be.getFilter().getItem(), 1), fac.getOpposite());
            be.setChanged();
        }
    }

    public ItemStack getFilter() {
        return filter;
    }

    public void setFilter(ItemStack filter) {
        if (filter == null) {
            this.filter = ItemStack.EMPTY;
        }
        this.filter = filter.copy();

        if (!filter.isEmpty()) {
            filterInv.setItem(0, filter.copy());
        } else {
            filterInv.setItem(0, ItemStack.EMPTY);
        }

        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void clearFilter() {
        this.filter = ItemStack.EMPTY;
        filterInv.setItem(0, ItemStack.EMPTY);
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public SimpleContainer getFilterInventory() {
        if (!filter.isEmpty() && filterInv.getItem(0).isEmpty()) {
            filterInv.setItem(0, filter.copy());
        } else if (filter.isEmpty() && !filterInv.getItem(0).isEmpty()) {
            filterInv.setItem(0, ItemStack.EMPTY);
        }
        return filterInv;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (filter != null && !filter.isEmpty()) {
            pTag.put("filter", filter.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("filter")) {
            this.filter = ItemStack.of(pTag.getCompound("filter"));
        } else {
            this.filter = ItemStack.EMPTY;
        }
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
        return NonNullList.of(ItemStack.EMPTY, this.filter);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.depot_unloader");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        getFilterInventory();
        return new DepotUnloaderScreenHandler(syncId, playerInventory, this, new ContainerData() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 1;
            }
        });
    }
}
