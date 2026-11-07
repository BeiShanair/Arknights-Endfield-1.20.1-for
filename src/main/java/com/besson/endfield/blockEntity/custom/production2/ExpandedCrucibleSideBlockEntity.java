package com.besson.endfield.blockEntity.custom.production2;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExpandedCrucibleSideBlockEntity extends BaseCrucibleSideBlockEntity {
    public ExpandedCrucibleSideBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXPANDED_CRUCIBLE_SIDE.get(), pos, state);
    }

    @Override
    public @Nullable ExpandedCrucibleBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity entity = level.getBlockEntity(parentPos);
        if (entity instanceof ExpandedCrucibleBlockEntity parent) {
            return parent;
        }
        return null;
    }
}
