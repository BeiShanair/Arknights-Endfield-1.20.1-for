package com.besson.endfield.block.custom.production1;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.production1.RefiningUnitBlockEntity;
import com.besson.endfield.blockEntity.custom.production1.RefiningUnitSideBlockEntity;
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

public class RefiningUnitBlock extends ModBlockEntityWithFacing {

    public RefiningUnitBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RefiningUnitBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.REFINING_UNIT.get(), RefiningUnitBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof RefiningUnitBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (RefiningUnitBlockEntity) entity, pPos);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide() && pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof RefiningUnitBlockEntity) {
                Containers.dropContents(pLevel, pPos, ((RefiningUnitBlockEntity) blockEntity).getItems());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            BlockPos[] adjacentPositions = getAdjacentPositions(pState, pPos);

            for (BlockPos p : adjacentPositions) {
                if (pLevel.getBlockState(p).getBlock() == ModBlocks.REFINING_UNIT_SIDE.get()) {
                    pLevel.destroyBlock(p, false);
                }
            }

        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            BlockPos[] adjacentPositions = getAdjacentPositions(pState, pos);

            for (BlockPos p : adjacentPositions) {
                pLevel.setBlockAndUpdate(p, ModBlocks.REFINING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)));
                BlockEntity be = pLevel.getBlockEntity(p);
                if (be instanceof RefiningUnitSideBlockEntity adjunct) {
                    adjunct.setParentPos(pos);
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

    private BlockPos[] getAdjacentPositions(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();
        Direction back = facing.getOpposite();
        Direction backLeft = back.getClockWise();
        Direction backRight = back.getCounterClockWise();

        return new BlockPos[]{
                pos.relative(facing), pos.relative(facing).relative(left),
                pos.relative(right), pos.relative(left),
                pos.relative(facing).relative(right), pos.relative(back),
                pos.relative(back).relative(backLeft), pos.relative(back).relative(backRight)
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("endfield.powerCost", 5).withStyle(ChatFormatting.GRAY));
    }
}
