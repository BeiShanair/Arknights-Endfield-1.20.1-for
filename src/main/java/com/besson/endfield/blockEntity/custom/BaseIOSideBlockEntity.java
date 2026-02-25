package com.besson.endfield.blockEntity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import static com.besson.endfield.block.ModBlockEntityWithFacing.FACING;

public abstract class BaseIOSideBlockEntity extends BlockEntity {
    protected BlockPos parentPos;
    public BaseIOSideBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setParentPos(BlockPos parentPos) {
        this.parentPos = parentPos;
        setChanged();
    }

    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (parentPos != null) {
            nbt.putLong("parent", parentPos.asLong());
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("parent")) {
            parentPos = BlockPos.of(nbt.getLong("parent"));
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

    public abstract @Nullable BaseIOBlockEntity<?> getParentBlock();
    
    public static <T extends BaseIOSideBlockEntity> void tick(Level world, BlockPos pos, BlockState state, T be) {
        if (world.isClientSide()) return;

        BaseIOBlockEntity<?> parent = be.getParentBlock();
        if (parent != null) {
            Direction facing = state.getValue(FACING);
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
}
