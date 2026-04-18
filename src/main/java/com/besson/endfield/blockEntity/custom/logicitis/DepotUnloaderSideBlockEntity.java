package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DepotUnloaderSideBlockEntity extends BlockEntity {
    private BlockPos parentPos;

    public DepotUnloaderSideBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DEPOT_UNLOADER_SIDE.get(), pos, state);
    }

    public void setParentPos(BlockPos pos) {
        this.parentPos = pos;
        setChanged();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("parent")) {
            parentPos = BlockPos.of(pTag.getLong("parent"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (parentPos != null) {
            pTag.putLong("parent", parentPos.asLong());
        }
    }

    @Override
    public @Nullable net.minecraft.network.protocol.Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Nullable
    public DepotUnloaderBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity be = level.getBlockEntity(parentPos);
        if (be instanceof DepotUnloaderBlockEntity parent) {
            return parent;
        }
        return null;
    }
}
