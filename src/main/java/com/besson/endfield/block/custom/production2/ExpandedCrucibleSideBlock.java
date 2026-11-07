package com.besson.endfield.block.custom.production2;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleSideBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.ExpandedCrucibleSideBlockEntity;
import com.besson.endfield.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExpandedCrucibleSideBlock extends BaseCrucibleSideBlock {
    public ExpandedCrucibleSideBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExpandedCrucibleSideBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends BaseCrucibleSideBlockEntity> sideType() {
        return ModBlockEntities.EXPANDED_CRUCIBLE_SIDE.get();
    }

    @Override
    protected Item getPickItem() {
        return ModItems.EXPANDED_CRUCIBLE_ITEM.get();
    }
}
