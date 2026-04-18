package com.besson.endfield.block.custom.production2;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.production2.GearingUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.GearingUnitSideBlockEntity;
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

public class GearingUnitBlock extends ModBlockEntityWithFacing {

    public GearingUnitBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GearingUnitBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GEARING_UNIT.get(), GearingUnitBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof GearingUnitBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (GearingUnitBlockEntity) entity, pPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide() && pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GearingUnitBlockEntity) {
                Containers.dropContents(pLevel, pPos, ((GearingUnitBlockEntity) blockEntity).getItems());
                pLevel.updateNeighborsAt(pPos, this);
            }

            BlockPos[] adjacentPositions = getAdjacentPositions(pState, pPos);

            for (BlockPos p : adjacentPositions) {
                if (pLevel.getBlockState(p).getBlock() == ModBlocks.GEARING_UNIT_SIDE.get()) {
                    pLevel.destroyBlock(p, false);
                }
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }

    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            BlockPos[] sidePositions = getAdjacentPositions(pState, pPos);

            for (BlockPos p : sidePositions) {
                pLevel.setBlockAndUpdate(p, ModBlocks.GEARING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)));
                BlockEntity blockEntity = pLevel.getBlockEntity(p);
                if (blockEntity instanceof GearingUnitSideBlockEntity sideBlockEntity) {
                    sideBlockEntity.setParentPos(pPos);
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
                pPos.relative(facing),
                pPos.relative(facing).relative(left), pPos.relative(facing).relative(right),
                pPos.relative(facing).relative(left, 2), pPos.relative(facing).relative(right, 2), pPos.relative(facing).relative(right, 3),
                pPos.relative(right), pPos.relative(left),
                pPos.relative(right, 2), pPos.relative(right, 3), pPos.relative(left, 2),
                pPos.relative(back), pPos.relative(back, 2),
                pPos.relative(back).relative(backLeft), pPos.relative(back).relative(backRight),
                pPos.relative(back).relative(backLeft, 2), pPos.relative(back).relative(backRight, 2), pPos.relative(back).relative(backRight, 3),
                pPos.relative(back, 2).relative(backLeft), pPos.relative(back, 2).relative(backRight),
                pPos.relative(back, 2).relative(backLeft, 2), pPos.relative(back, 2).relative(backRight, 2), pPos.relative(back, 2).relative(backRight, 3)
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("endfield.powerCost", 10).withStyle(ChatFormatting.GRAY));
    }
}
