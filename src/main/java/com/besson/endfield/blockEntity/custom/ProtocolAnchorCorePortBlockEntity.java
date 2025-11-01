package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.custom.ProtocolAnchorCorePortBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProtocolAnchorCorePortBlockEntity extends BlockEntity {

    private BlockPos parentPos;
    private ItemStack filter = ItemStack.EMPTY;

    private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(InputHandler::new);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(FilteredOutputHandler::new);


    protected ProtocolAnchorCorePortBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ProtocolAnchorCorePortBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ModBlockEntities.PROTOCOL_ANCHOR_CORE_PORT.get(), blockPos, blockState);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ProtocolAnchorCorePortBlockEntity entity) {
        if (world.isClientSide()) return;
        if (entity.parentPos != null) return;

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
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void clearFilter() {
        this.filter = ItemStack.EMPTY;
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
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
            // 输出面不允许输入
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
