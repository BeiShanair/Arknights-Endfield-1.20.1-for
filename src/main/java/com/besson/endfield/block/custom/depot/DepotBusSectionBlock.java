package com.besson.endfield.block.custom.depot;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.custom.depot.DepotBusSectionBlockEntity;
import com.besson.endfield.blockEntity.custom.depot.DepotBusSectionSideBlockEntity;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DepotBusSectionBlock extends ModBlockEntityWithFacing {
    public DepotBusSectionBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DepotBusSectionBlockEntity(pos, state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos[] sidePositions = getAdjacentPositions(state, pos);

        for (BlockPos p : sidePositions) {
            if (!MultiBlockPlacementUtil.isReplaceable(world, p)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockPos[] sidePositions = getAdjacentPositions(state, pos);

            for (BlockPos p : sidePositions) {
                world.destroyBlock(p, false);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide()) {
            BlockPos[] sidePos = getAdjacentPositions(state, pos);
            for (BlockPos p : sidePos) {
                world.setBlockAndUpdate(p, ModBlocks.DEPOT_BUS_SECTION_SIDE.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
                BlockEntity blockEntity = world.getBlockEntity(p);
                if (blockEntity instanceof DepotBusSectionSideBlockEntity side) {
                    side.setParentPos(pos);
                }
            }
        }
    }

    private BlockPos[] getAdjacentPositions(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        Direction back = facing.getOpposite();

        return new BlockPos[]{
                pos.relative(facing), pos.relative(back),
                pos.relative(facing).above(), pos.relative(back).above(), pos.above(),
                pos.relative(facing).above(2), pos.relative(back).above(2), pos.above(2)
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("endfield.require_area", "1×3").withStyle(ChatFormatting.GRAY));
    }
}
