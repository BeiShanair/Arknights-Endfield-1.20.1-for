package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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

public class ProtocolStashSideBlockEntity extends BlockEntity {
    private BlockPos parentPos;

    public ProtocolStashSideBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PROTOCOL_STASH_SIDE.get(), pos, state);
    }

    public void setParentPos(BlockPos parentPos) {
        this.parentPos = parentPos;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (parentPos != null) {
            pTag.putLong("parent", parentPos.asLong());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("parent")) {
            parentPos = BlockPos.of(pTag.getLong("parent"));
        }
    }

    @Nullable
    public ProtocolStashBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity entity = this.level.getBlockEntity(parentPos);
        if (entity instanceof ProtocolStashBlockEntity parent) {
            return parent;
        }

        return null;
    }

    @Override
    public @Nullable net.minecraft.network.protocol.Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ProtocolStashSideBlockEntity be) {
        if (world.isClientSide()) return;

        ProtocolStashBlockEntity parent = be.getParentBlock();
        if (parent != null) {
            Direction facing = state.getValue(ModBlockEntityWithFacing.FACING);
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
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            ProtocolStashBlockEntity parent = getParentBlock();
            if (parent != null) {
                return parent.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }
}
