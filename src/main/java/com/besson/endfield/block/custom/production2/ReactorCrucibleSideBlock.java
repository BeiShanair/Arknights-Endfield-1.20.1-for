package com.besson.endfield.block.custom.production2;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleSideBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.ReactorCrucibleSideBlockEntity;
import com.besson.endfield.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ReactorCrucibleSideBlock extends BaseCrucibleSideBlock {
    public ReactorCrucibleSideBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorCrucibleSideBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends BaseCrucibleSideBlockEntity> sideType() {
        return ModBlockEntities.REACTOR_CRUCIBLE_SIDE.get();
    }

    @Override
    protected Item getPickItem() {
        return ModItems.REACTOR_CRUCIBLE_ITEM.get();
    }
}
