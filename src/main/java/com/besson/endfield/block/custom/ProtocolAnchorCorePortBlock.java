package com.besson.endfield.block.custom;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.ProtocolAnchorCoreBlockEntity;
import com.besson.endfield.blockEntity.custom.ProtocolAnchorCorePortBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class ProtocolAnchorCorePortBlock extends ModBlockEntityWithFacing {

    public ProtocolAnchorCorePortBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ProtocolAnchorCorePortBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) return InteractionResult.CONSUME;

        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (!(entity instanceof ProtocolAnchorCorePortBlockEntity entity1)) return InteractionResult.CONSUME;

        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if (heldItem.isEmpty() && !pPlayer.isShiftKeyDown()) {
            ProtocolAnchorCoreBlockEntity parent = entity1.getParentBlock();
            if (parent != null) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, parent, parent::writeScreenData);
                return InteractionResult.SUCCESS;
            }
        }

        if (heldItem.isEmpty() && pPlayer.isShiftKeyDown()) {
            entity1.clearFilter();
            if (pPlayer instanceof ServerPlayer) {
                ((ServerPlayer) pPlayer).displayClientMessage(Component.literal("Cleared filter"), false);
            }
        } else {
            entity1.setFilter(heldItem);
            if (pPlayer instanceof ServerPlayer) {
                ((ServerPlayer) pPlayer).displayClientMessage(Component.literal("Set filter to: " + heldItem.getHoverName().getString()), false);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.PROTOCOL_ANCHOR_CORE_PORT.get(),
                (world1, pos, state1, blockEntity) ->
                        ProtocolAnchorCorePortBlockEntity.tick(world1, pos, state1, (ProtocolAnchorCorePortBlockEntity) blockEntity));
    }
}
