package com.besson.endfield.block.custom.production2;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleSideBlockEntity;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseCrucibleBlock extends ModBlockEntityWithFacing {
    private final int powerCost;
    private final String areaLabel;

    protected BaseCrucibleBlock(Properties properties, int powerCost, String areaLabel) {
        super(properties);
        this.powerCost = powerCost;
        this.areaLabel = areaLabel;
    }
    
    protected abstract Block getSideBlock();
    
    protected abstract BlockEntityType<? extends BaseCrucibleBlockEntity> crucibleType();

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof BaseCrucibleBlockEntity crucible && player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, crucible, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, crucibleType(), BaseCrucibleBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BaseCrucibleBlockEntity be) {
                for (BaseCrucibleBlockEntity.StoredResource resource : be.getDisplayResources()) {
                    if (resource.item()) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), resource.asStack());
                    }
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }

            for (BlockPos p : getAdjacentPositions(state, pos)) {
                if (level.getBlockState(p).getBlock() == getSideBlock()) {
                    level.destroyBlock(p, false);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide()) {
            for (BlockPos p : getAdjacentPositions(state, pos)) {
                level.setBlockAndUpdate(p, getSideBlock().defaultBlockState().setValue(FACING, state.getValue(FACING)));
                BlockEntity blockEntity = level.getBlockEntity(p);
                if (blockEntity instanceof BaseCrucibleSideBlockEntity sideBlockEntity) {
                    sideBlockEntity.setParentPos(pos);
                }
            }
        }
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

    protected BlockPos[] getAdjacentPositions(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();
        Direction back = facing.getOpposite();
        Direction backLeft = back.getClockWise();
        Direction backRight = back.getCounterClockWise();

        return new BlockPos[]{
                pos.relative(facing),
                pos.relative(facing).relative(left), pos.relative(facing).relative(right),
                pos.relative(facing).relative(left, 2), pos.relative(facing).relative(right, 2),
                pos.relative(facing).relative(right, 3),
                pos.relative(facing, 2),
                pos.relative(facing, 2).relative(left), pos.relative(facing, 2).relative(right),
                pos.relative(facing, 2).relative(left, 2), pos.relative(facing, 2).relative(right, 2), pos.relative(facing, 2).relative(right, 3),
                pos.relative(right), pos.relative(left),
                pos.relative(right, 2), pos.relative(left, 2),
                pos.relative(back), pos.relative(back, 2),
                pos.relative(back).relative(backLeft), pos.relative(back).relative(backRight),
                pos.relative(back).relative(backLeft, 2), pos.relative(back).relative(backRight, 2), pos.relative(back).relative(backRight, 3),
                pos.relative(back, 2).relative(backLeft), pos.relative(back, 2).relative(backRight), pos.relative(back, 2).relative(backRight, 3),
                pos.relative(back, 2).relative(backLeft, 2), pos.relative(back, 2).relative(backRight, 2), pos.relative(back, 2).relative(backRight, 3),
                pos.relative(facing).relative(right, 3).above(2),
                pos.relative(facing).relative(left, 2).above(2),
                pos.relative(back).relative(right, 3).above(2),
                pos.relative(back).relative(left, 2).above(2)
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("endfield.powerCost", powerCost).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("endfield.require_area", areaLabel).withStyle(ChatFormatting.GRAY));
    }
}
