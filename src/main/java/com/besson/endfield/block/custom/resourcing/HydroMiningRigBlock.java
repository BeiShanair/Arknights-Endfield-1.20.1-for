package com.besson.endfield.block.custom.resourcing;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.resourcing.HydroMiningRigBlockEntity;
import com.besson.endfield.blockEntity.custom.resourcing.HydroMiningRigSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class HydroMiningRigBlock extends ModBlockEntityWithFacing {
    public HydroMiningRigBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HydroMiningRigBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.HYDRO_MINING_RIG.get(), HydroMiningRigBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HydroMiningRigBlockEntity be) {
                NetworkHooks.openScreen((ServerPlayer) player, be, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide()) return;

        for (BlockPos side : getAdjacentPositions(state, pos)) {
            level.setBlockAndUpdate(side, ModBlocks.HYDRO_MINING_RIG_SIDE.get().defaultBlockState()
                    .setValue(FACING, state.getValue(FACING)));
            if (level.getBlockEntity(side) instanceof HydroMiningRigSideBlockEntity sideBlock) {
                sideBlock.setParentPos(pos);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof HydroMiningRigBlockEntity be) {
                Containers.dropContents(level, pos, be.getItems());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            for (BlockPos side : getAdjacentPositions(state, pos)) {
                if (level.getBlockState(side).getBlock() == ModBlocks.HYDRO_MINING_RIG_SIDE.get()) {
                    // 与 Fab 一致：侧方块直接移除，不掉落
                    level.removeBlock(side, false);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (BlockPos p : getAdjacentPositions(state, pos)) {
            if (!MultiBlockPlacementUtil.isReplaceable(level, p)) {
                return false;
            }
        }
        return true;
    }
    
    private BlockPos[] getAdjacentPositions(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return new BlockPos[]{
                pos.relative(facing).above(2)
        };
    }
}
