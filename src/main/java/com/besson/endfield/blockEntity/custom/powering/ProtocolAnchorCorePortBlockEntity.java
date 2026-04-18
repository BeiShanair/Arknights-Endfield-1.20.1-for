package com.besson.endfield.blockEntity.custom.powering;

import com.besson.endfield.block.custom.powering.ProtocolAnchorCorePortBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.logicitis.BeltBlockEntity;
import com.besson.endfield.screen.custom.powering.ProtocolAnchorCorePortScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProtocolAnchorCorePortBlockEntity extends BlockEntity implements MenuProvider {

    private BlockPos parentPos;
    private ItemStack filter = ItemStack.EMPTY;

    private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(InputHandler::new);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(FilteredOutputHandler::new);

    private final SimpleContainer filterInventory = new SimpleContainer(1) {

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    public ProtocolAnchorCorePortBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.PROTOCOL_ANCHOR_CORE_PORT.get(), blockPos, blockState);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ProtocolAnchorCorePortBlockEntity entity) {
        if (world.isClientSide()) return;

        if (entity.parentPos != null) {
            ProtocolAnchorCoreBlockEntity parent = entity.getParentBlock();
            if (parent != null) {
                Direction facing = state.getValue(ProtocolAnchorCorePortBlock.FACING);
                BlockEntity machineBe = world.getBlockEntity(pos);
                if (machineBe == null) return;
                LazyOptional<IItemHandler> optional =
                        machineBe.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite());

                if (!optional.isPresent()) return;

                IItemHandler handler = optional.orElse(null);

                BlockPos beltPos = pos.relative(facing.getOpposite());
                BlockEntity targetBe = world.getBlockEntity(beltPos);

                if (!(targetBe instanceof BeltBlockEntity belt)) return;

                if (!belt.canAcceptFrom(facing)) return;

                for (int i = 0; i < handler.getSlots(); i++) {

                    ItemStack simulated = handler.extractItem(i, 1, true);

                    if (!simulated.isEmpty()) {

                        ItemStack extracted = handler.extractItem(i, 1, false);

                        if (!extracted.isEmpty()) {
                            belt.acceptItem(extracted, facing);
                            return;
                        }
                    }
                }
            }
            return;
        }
        
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(4, 0, 4), pos.offset(-4, 0, -4))) {
            BlockEntity checkEntity = world.getBlockEntity(p);
            if (checkEntity instanceof ProtocolAnchorCoreBlockEntity) {
                entity.setParentPos(p);
                entity.setChanged();
                break;
            }
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
            filterInventory.setItem(0, filter.copy());
        } else {
            filterInventory.setItem(0, ItemStack.EMPTY);
        }
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void clearFilter() {
        this.filter = ItemStack.EMPTY;
        filterInventory.setItem(0, ItemStack.EMPTY);
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public SimpleContainer getFilterInventory() {
        if (!filter.isEmpty() && filterInventory.getItem(0).isEmpty()) {
            filterInventory.setItem(0, filter.copy());
        } else if (filter.isEmpty() && !filterInventory.getItem(0).isEmpty()) {
            filterInventory.setItem(0, ItemStack.EMPTY);
        }
        return filterInventory;
    }
    
    public @Nullable ProtocolAnchorCoreBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity entity = level.getBlockEntity(parentPos);
        if (entity instanceof ProtocolAnchorCoreBlockEntity parentBlock) {
            return parentBlock;
        }
        return null;
    }

    public NonNullList<ItemStack> getItems() {
        ProtocolAnchorCoreBlockEntity parent = this.getParentBlock();
        if (parent != null) {
            return parent.getItems();
        }
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    public void setParentPos(BlockPos parentPos) {
        this.parentPos = parentPos;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (parentPos != null) {
            pTag.putLong("parentPos", parentPos.asLong());
        }
        if (filter != null && !filter.isEmpty()) {
            CompoundTag filterTag = new CompoundTag();
            filter.save(filterTag);
            pTag.put("filter", filterTag);
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("parentPos")) {
            this.parentPos = BlockPos.of(pTag.getLong("parentPos"));
        }
        if (pTag.contains("filter")) {
            CompoundTag filterTag = pTag.getCompound("filter");
            this.filter = ItemStack.of(filterTag);
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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            Direction facing = getBlockState().getValue(ProtocolAnchorCorePortBlock.FACING);
            if (side == facing) {
                return inputHandler.cast(); // 输入面
            } else if (side == facing.getOpposite()) {
                return outputHandler.cast(); // 输出面
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.protocol_anchor_core_port");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        getFilterInventory();
        return new ProtocolAnchorCorePortScreenHandler(pContainerId, pPlayerInventory, this, new ContainerData() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {}

            @Override
            public int getCount() {
                return 1;
            }
        });
    }

    private class InputHandler implements IItemHandler {
        @Override
        public int getSlots() {
            ProtocolAnchorCoreBlockEntity parent = getParentBlock();
            return parent != null ? parent.getItemHandler().getSlots() : 0;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            ProtocolAnchorCoreBlockEntity parent = getParentBlock();
            return parent != null ? parent.getItemHandler().getStackInSlot(slot) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ProtocolAnchorCoreBlockEntity parent = getParentBlock();
            return parent != null ? parent.getItemHandler().insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            // 输入面不允许取出
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return true;
        }
    }

    private class FilteredOutputHandler implements IItemHandler {
        @Override
        public int getSlots() {
            ProtocolAnchorCoreBlockEntity parent = getParentBlock();
            return parent != null ? parent.getItemHandler().getSlots() : 0;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            ProtocolAnchorCoreBlockEntity parent = getParentBlock();
            if (parent == null) return ItemStack.EMPTY;

            ItemStack stack = parent.getItemHandler().getStackInSlot(slot);
            if (filter.isEmpty() || stack.isEmpty() || stack.getItem() == filter.getItem()) {
                return stack;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ProtocolAnchorCoreBlockEntity parent = getParentBlock();
            if (parent == null) return ItemStack.EMPTY;

            ItemStack stackInSlot = parent.getItemHandler().getStackInSlot(slot);
            if (filter.isEmpty() || stackInSlot.getItem() == filter.getItem()) {
                return parent.getItemHandler().extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }
    }
}
