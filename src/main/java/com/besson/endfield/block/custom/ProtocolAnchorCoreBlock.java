package com.besson.endfield.block.custom;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.ProtocolAnchorCoreBlockEntity;
import net.minecraft.core.BlockPos;
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

public class ProtocolAnchorCoreBlock extends ModBlockEntityWithFacing {

    public ProtocolAnchorCoreBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ProtocolAnchorCoreBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ProtocolAnchorCoreBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (ProtocolAnchorCoreBlockEntity) blockEntity, friendlyByteBuf -> {
                    ((ProtocolAnchorCoreBlockEntity) blockEntity).writeScreenData(friendlyByteBuf);
                });
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.PROTOCOL_ANCHOR_CORE.get(),
                (world1, pos, state1, blockEntity) ->
                        ProtocolAnchorCoreBlockEntity.tick(world1, pos, state1, (ProtocolAnchorCoreBlockEntity) blockEntity));
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            for (BlockPos p: BlockPos.betweenClosed(pPos.offset(4, 0, 4), pPos.offset(-4, 0, -4))) {
                BlockState checkState = pLevel.getBlockState(p);
                if (checkState.is(this)) {
                    continue;
                }
                pLevel.setBlockAndUpdate(p, ModBlocks.PROTOCOL_ANCHOR_CORE_SIDE.get().defaultBlockState());
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide() && pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ProtocolAnchorCoreBlockEntity entity) {
                Containers.dropContents(pLevel, pPos, entity.getItems());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            for (BlockPos p: BlockPos.betweenClosed(pPos.offset(4, 0, 4), pPos.offset(-4, 0, -4))) {
                BlockState checkState = pLevel.getBlockState(p);
                if (checkState.is(ModBlocks.PROTOCOL_ANCHOR_CORE_SIDE.get())) {
                    pLevel.destroyBlock(p, false);
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
