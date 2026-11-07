package com.besson.endfield.blockEntity.custom.production2;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ReactorCrucibleSideBlockEntity extends BaseCrucibleSideBlockEntity {
    public ReactorCrucibleSideBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REACTOR_CRUCIBLE_SIDE.get(), pos, state);
    }

    @Override
    public @Nullable ReactorCrucibleBlockEntity getParentBlock() {
        if (parentPos == null || level == null) return null;
        BlockEntity entity = level.getBlockEntity(parentPos);
        if (entity instanceof ReactorCrucibleBlockEntity parent) {
            return parent;
        }
        return null;
    }
}
