package com.besson.endfield.block.custom.resourcing;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.resourcing.AcidResistantPumpMkIIBlockEntity;
import com.besson.endfield.blockEntity.custom.resourcing.AcidResistantPumpMkIISideBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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

public class AcidResistantPumpMkIIBlock extends ModBlockEntityWithFacing {
    public AcidResistantPumpMkIIBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AcidResistantPumpMkIIBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ACID_RESISTANT_PUMP_MK_II.get(), AcidResistantPumpMkIIBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AcidResistantPumpMkIIBlockEntity be) {
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
            level.setBlockAndUpdate(side, ModBlocks.ACID_RESISTANT_PUMP_MK_II_SIDE.get().defaultBlockState()
                    .setValue(FACING, state.getValue(FACING)));
            if (level.getBlockEntity(side) instanceof AcidResistantPumpMkIISideBlockEntity sideBlock) {
                sideBlock.setParentPos(pos);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            for (BlockPos side : getAdjacentPositions(state, pos)) {
                if (level.getBlockState(side).getBlock() == ModBlocks.ACID_RESISTANT_PUMP_MK_II_SIDE.get()) {
                    level.destroyBlock(side, false);
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("endfield.powerCost", 20).withStyle(ChatFormatting.GRAY));
    }
}
