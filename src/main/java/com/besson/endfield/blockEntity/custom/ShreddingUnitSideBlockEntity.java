package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShreddingUnitSideBlockEntity extends BaseIOSideBlockEntity {
    public ShreddingUnitSideBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHREDDING_UNIT_SIDE.get(), pos, state);
    }

    public @Nullable ShreddingUnitBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity entity = level.getBlockEntity(parentPos);
        if (entity instanceof ShreddingUnitBlockEntity entity1) {
            return entity1;
        }
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            ShreddingUnitBlockEntity parent = getParentBlock();
            if (parent != null) {
                return parent.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }
}
