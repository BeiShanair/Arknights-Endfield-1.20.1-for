package com.besson.endfield.block.custom;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.FittingUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.FittingUnitSideBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class FittingUnitBlock extends ModBlockEntityWithFacing {

    public FittingUnitBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FittingUnitBlockEntity(pPos, pState);

    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.FITTING_UNIT.get(),
                (world1, pos, state1, blockEntity) ->
                        FittingUnitBlockEntity.tick(world1, pos, state1, (FittingUnitBlockEntity) blockEntity));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof FittingUnitBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (FittingUnitBlockEntity) entity, pPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide() && pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof FittingUnitBlockEntity) {
                Containers.dropContents(pLevel, pPos, ((FittingUnitBlockEntity)blockEntity).getItems());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            Direction facing = pState.getValue(FACING);
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();
            Direction backLeft = back.getClockWise();
            Direction backRight = back.getCounterClockWise();

            BlockPos[] adjacentPositions = {
                    pPos.relative(facing), pPos.relative(facing).relative(left),
                    pPos.relative(right), pPos.relative(left),
                    pPos.relative(facing).relative(right), pPos.relative(back),
                    pPos.relative(back).relative(backLeft), pPos.relative(back).relative(backRight)
            };

            for (BlockPos p : adjacentPositions) {
                if (pLevel.getBlockState(p).getBlock() == ModBlocks.FITTING_UNIT_SIDE.get()) {
                    pLevel.destroyBlock(p, false);
                }
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            Direction facing = pState.getValue(FACING);
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();
            Direction backLeft = back.getClockWise();
            Direction backRight = back.getCounterClockWise();

            BlockPos[] adjacentPositions = {
                    pPos.relative(facing), pPos.relative(facing).relative(left),
                    pPos.relative(right), pPos.relative(left),
                    pPos.relative(facing).relative(right), pPos.relative(back),
                    pPos.relative(back).relative(backLeft), pPos.relative(back).relative(backRight)
            };

            for (BlockPos p : adjacentPositions) {
                pLevel.setBlockAndUpdate(p, ModBlocks.FITTING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, facing));
                BlockEntity be = pLevel.getBlockEntity(p);
                if (be instanceof FittingUnitSideBlockEntity adjunct) {
                    adjunct.setParentPos(pPos);
                }
            }
        }
    }
}
