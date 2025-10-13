package com.besson.endfield.block.custom;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.SeedPickingUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.SeedPickingUnitSideBlockEntity;
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

public class SeedPickingUnitBlock extends ModBlockEntityWithFacing {

    public SeedPickingUnitBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SeedPickingUnitBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide() && pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SeedPickingUnitBlockEntity) {
                Containers.dropContents(pLevel, pPos, ((SeedPickingUnitBlockEntity) blockEntity).getItems());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            Direction facing = pState.getValue(FACING);
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();
            Direction backLeft = back.getClockWise();
            Direction backRight = back.getCounterClockWise();

            BlockPos[] adjacentPositions = {
                    pPos.relative(facing),
                    pPos.relative(facing).relative(left), pPos.relative(facing).relative(right),
                    pPos.relative(facing).relative(left, 2), pPos.relative(facing).relative(right, 2),
                    pPos.relative(right), pPos.relative(left),
                    pPos.relative(right, 2), pPos.relative(left, 2),
                    pPos.relative(back), pPos.relative(back, 2),
                    pPos.relative(back).relative(backLeft), pPos.relative(back).relative(backRight),
                    pPos.relative(back).relative(backLeft, 2), pPos.relative(back).relative(backRight, 2),
                    pPos.relative(back, 2).relative(backLeft), pPos.relative(back, 2).relative(backRight),
                    pPos.relative(back, 2).relative(backLeft, 2), pPos.relative(back, 2).relative(backRight, 2)
            };

            for (BlockPos p : adjacentPositions) {
                if (pLevel.getBlockState(p).getBlock() == ModBlocks.SEED_PICKING_UNIT_SIDE.get()) {
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

            BlockPos[] sidePositions = {
                    pPos.relative(facing),
                    pPos.relative(facing).relative(left), pPos.relative(facing).relative(right),
                    pPos.relative(facing).relative(left, 2), pPos.relative(facing).relative(right, 2),
                    pPos.relative(right), pPos.relative(left),
                    pPos.relative(right, 2), pPos.relative(left, 2),
                    pPos.relative(back), pPos.relative(back, 2),
                    pPos.relative(back).relative(backLeft), pPos.relative(back).relative(backRight),
                    pPos.relative(back).relative(backLeft, 2), pPos.relative(back).relative(backRight, 2),
                    pPos.relative(back, 2).relative(backLeft), pPos.relative(back, 2).relative(backRight),
                    pPos.relative(back, 2).relative(backLeft, 2), pPos.relative(back, 2).relative(backRight, 2)
            };

            for (BlockPos p : sidePositions) {
                pLevel.setBlockAndUpdate(p, ModBlocks.SEED_PICKING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)));
                BlockEntity blockEntity = pLevel.getBlockEntity(p);
                if (blockEntity instanceof SeedPickingUnitSideBlockEntity sideBlockEntity) {
                    sideBlockEntity.setParentPos(pPos);
                }
            }

        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.SEED_PICKING_UNIT.get(),
                (world1, pos, state1, blockEntity) ->
                        SeedPickingUnitBlockEntity.tick(world1, pos, state1, (SeedPickingUnitBlockEntity) blockEntity));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof SeedPickingUnitBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (SeedPickingUnitBlockEntity) entity, pPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }
}
