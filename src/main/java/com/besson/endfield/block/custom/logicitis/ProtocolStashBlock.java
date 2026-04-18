package com.besson.endfield.block.custom.logicitis;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.logicitis.ProtocolStashBlockEntity;
import com.besson.endfield.blockEntity.custom.logicitis.ProtocolStashSideBlockEntity;
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

public class ProtocolStashBlock extends ModBlockEntityWithFacing {

    public ProtocolStashBlock(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof ProtocolStashBlockEntity){
                NetworkHooks.openScreen((ServerPlayer) player, (ProtocolStashBlockEntity) entity, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ProtocolStashBlockEntity chest) {
                Containers.dropContents(world, pos, chest.getItems());
                world.updateNeighbourForOutputSignal(pos, this);
            }

            BlockPos[] sidePos = getAdjacentPositions(state, pos);
            for (BlockPos p : sidePos) {
                if (world.getBlockState(p).is(ModBlocks.PROTOCOL_STASH_SIDE.get())) {
                    world.destroyBlock(p, false);
                }
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ProtocolStashBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.PROTOCOL_STASH.get(), ProtocolStashBlockEntity::tick);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide()) {
            BlockPos[] sidePos = getAdjacentPositions(state, pos);

            for (BlockPos p : sidePos) {
                world.setBlockAndUpdate(p, ModBlocks.PROTOCOL_STASH_SIDE.get().defaultBlockState().setValue(FACING, state.getValue(FACING)));
                BlockEntity blockEntity = world.getBlockEntity(p);
                if (blockEntity instanceof ProtocolStashSideBlockEntity sideBlockEntity) {
                    sideBlockEntity.setParentPos(pos);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (!world.isClientSide()) {
            BlockPos[] sidePos = getAdjacentPositions(state, pos);
            for (BlockPos p : sidePos) {
                if (!world.getBlockState(p).getBlock().defaultBlockState().isAir()) {
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
                pos.relative(facing),
                pos.relative(facing).relative(left), pos.relative(facing).relative(right),
                pos.relative(right), pos.relative(left),
                pos.relative(back),
                pos.relative(back).relative(backLeft), pos.relative(back).relative(backRight)
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("protocol_stash.tooltip"));
    }
}
