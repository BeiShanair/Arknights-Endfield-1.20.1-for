package com.besson.endfield.block.custom.production1;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.production1.FittingUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.production1.FittingUnitSideBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
        return createTickerHelper(pBlockEntityType, ModBlockEntities.FITTING_UNIT.get(), FittingUnitBlockEntity::tick);
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
            BlockPos[] adjacentPositions = getAdjacentPositions(pState, pPos);

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
            BlockPos[] adjacentPositions = getAdjacentPositions(pState, pPos);

            for (BlockPos p : adjacentPositions) {
                pLevel.setBlockAndUpdate(p, ModBlocks.FITTING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)));
                BlockEntity be = pLevel.getBlockEntity(p);
                if (be instanceof FittingUnitSideBlockEntity adjunct) {
                    adjunct.setParentPos(pPos);
                }
            }
        }
    }
    
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide()) {
            BlockPos[] sidePositions = getAdjacentPositions(pState, pPos);

            for (BlockPos p : sidePositions) {
                if (!pLevel.getBlockState(p).getBlock().defaultBlockState().isAir()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private BlockPos[] getAdjacentPositions(BlockState state, BlockPos pPos) {
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();
        Direction back = facing.getOpposite();
        Direction backLeft = back.getClockWise();
        Direction backRight = back.getCounterClockWise();

        return new BlockPos[]{
                pPos.relative(facing), pPos.relative(facing).relative(left),
                pPos.relative(right), pPos.relative(left),
                pPos.relative(facing).relative(right), pPos.relative(back),
                pPos.relative(back).relative(backLeft), pPos.relative(back).relative(backRight)
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("endfield.powerCost", 20).withStyle(ChatFormatting.GRAY));
    }
}
