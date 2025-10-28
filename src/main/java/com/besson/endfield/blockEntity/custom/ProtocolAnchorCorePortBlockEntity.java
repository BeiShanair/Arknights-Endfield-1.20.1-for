package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.custom.ProtocolAnchorCorePortBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.Nullable;

public class ProtocolAnchorCorePortBlockEntity extends BlockEntity {

    private BlockPos parentPos;

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
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("parentPos")) {
            this.parentPos = BlockPos.of(pTag.getLong("parentPos"));
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

}
