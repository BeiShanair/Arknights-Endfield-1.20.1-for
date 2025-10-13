package com.besson.endfield.block.custom;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.GrindingUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.GrindingUnitSideBlockEntity;
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

public class GrindingUnitBlock extends ModBlockEntityWithFacing {

    public GrindingUnitBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GrindingUnitBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GRINDING_UNIT.get(),
                (world1, pos, state1, blockEntity) ->
                        GrindingUnitBlockEntity.tick(world1, pos, state1, (GrindingUnitBlockEntity) blockEntity));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof GrindingUnitBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (GrindingUnitBlockEntity) entity, pPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide() && pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pos);
            if (blockEntity instanceof GrindingUnitBlockEntity) {
                Containers.dropContents(pLevel, pos, ((GrindingUnitBlockEntity) blockEntity).getItems());
                pLevel.updateNeighbourForOutputSignal(pos, this);
            }

            Direction facing = pState.getValue(FACING);
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();
            Direction backLeft = back.getClockWise();
            Direction backRight = back.getCounterClockWise();

            BlockPos[] adjacentPositions = {
                    pos.relative(facing),
                    pos.relative(facing).relative(left), pos.relative(facing).relative(right),
                    pos.relative(facing).relative(left, 2), pos.relative(facing).relative(right, 2), pos.relative(facing).relative(right, 3),
                    pos.relative(right), pos.relative(left),
                    pos.relative(right, 2), pos.relative(right, 3), pos.relative(left, 2),
                    pos.relative(back), pos.relative(back, 2),
                    pos.relative(back).relative(backLeft), pos.relative(back).relative(backRight),
                    pos.relative(back).relative(backLeft, 2), pos.relative(back).relative(backRight, 2), pos.relative(back).relative(backRight, 3),
                    pos.relative(back, 2).relative(backLeft), pos.relative(back, 2).relative(backRight),
                    pos.relative(back, 2).relative(backLeft, 2), pos.relative(back, 2).relative(backRight, 2), pos.relative(back, 2).relative(backRight, 3)
            };

            for (BlockPos p : adjacentPositions) {
                if (pLevel.getBlockState(p).getBlock() == ModBlocks.GRINDING_UNIT_SIDE.get()) {
                    pLevel.destroyBlock(p, false);
                }
            }
            
            super.onRemove(pState, pLevel, pos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            Direction facing = pState.getValue(FACING);
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();
            Direction backLeft = back.getClockWise();
            Direction backRight = back.getCounterClockWise();

            BlockPos[] sidePositions = {
                    pos.relative(facing),
                    pos.relative(facing).relative(left), pos.relative(facing).relative(right),
                    pos.relative(facing).relative(left, 2), pos.relative(facing).relative(right, 2), pos.relative(facing).relative(right, 3),
                    pos.relative(right), pos.relative(left),
                    pos.relative(right, 2), pos.relative(right, 3), pos.relative(left, 2),
                    pos.relative(back), pos.relative(back, 2),
                    pos.relative(back).relative(backLeft), pos.relative(back).relative(backRight),
                    pos.relative(back).relative(backLeft, 2), pos.relative(back).relative(backRight, 2), pos.relative(back).relative(backRight, 3),
                    pos.relative(back, 2).relative(backLeft), pos.relative(back, 2).relative(backRight),
                    pos.relative(back, 2).relative(backLeft, 2), pos.relative(back, 2).relative(backRight, 2), pos.relative(back, 2).relative(backRight, 3)
            };

            for (BlockPos p : sidePositions) {
                pLevel.setBlockAndUpdate(p, ModBlocks.GRINDING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)));
                BlockEntity blockEntity = pLevel.getBlockEntity(p);
                if (blockEntity instanceof GrindingUnitSideBlockEntity sideBlockEntity) {
                    sideBlockEntity.setParentPos(pos);
                }
            }
        }
    }
}
