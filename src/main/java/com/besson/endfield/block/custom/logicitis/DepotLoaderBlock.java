package com.besson.endfield.block.custom.logicitis;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.logicitis.DepotLoaderBlockEntity;
import com.besson.endfield.blockEntity.custom.logicitis.DepotLoaderSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DepotLoaderBlock extends ModBlockEntityWithFacing {
    public DepotLoaderBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DepotLoaderBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.DEPOT_LOADER.get(), DepotLoaderBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockPos[] adjacentPositions = getAdjacentPositions(state, pos);
            for (BlockPos p : adjacentPositions) {
                if (world.getBlockState(p).getBlock() == ModBlocks.DEPOT_LOADER_SIDE.get()) {
                    world.destroyBlock(p, false);
                }
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide()) {
            BlockPos[] sidePos = getAdjacentPositions(state, pos);

            for (BlockPos p : sidePos) {
                world.setBlockAndUpdate(p, ModBlocks.DEPOT_LOADER_SIDE.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
                BlockEntity be = world.getBlockEntity(p);
                if (be instanceof DepotLoaderSideBlockEntity side) {
                    side.setParentPos(pos);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (!world.isClientSide()) {
            BlockPos[] side = getAdjacentPositions(state, pos);
            for (BlockPos p : side) {
                if (!world.getBlockState(p).getBlock().defaultBlockState().isAir()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private BlockPos[] getAdjacentPositions(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        return new BlockPos[]{
                pos.relative(left), pos.relative(left).above(),
                pos.above(),
                pos.relative(right), pos.relative(right).above()
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("depot_unloader.tooltip"));
    }
}
