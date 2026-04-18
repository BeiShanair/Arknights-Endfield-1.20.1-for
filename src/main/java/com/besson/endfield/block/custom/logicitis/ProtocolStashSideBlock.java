package com.besson.endfield.block.custom.logicitis;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.logicitis.ProtocolStashBlockEntity;
import com.besson.endfield.blockEntity.custom.logicitis.ProtocolStashSideBlockEntity;
import com.besson.endfield.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class ProtocolStashSideBlock extends ModBlockEntityWithFacing {
    public ProtocolStashSideBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ProtocolStashSideBlockEntity(pos, state);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ProtocolStashSideBlockEntity sideBlockEntity) {
                ProtocolStashBlockEntity parent = sideBlockEntity.getParentBlock();
                if (parent != null) {
                    BlockPos parentPos = parent.getBlockPos();
                    world.destroyBlock(parentPos, true);
                }
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof ProtocolStashSideBlockEntity entity1) {
                ProtocolStashBlockEntity parent = entity1.getParentBlock();
                if (parent != null) {
                    NetworkHooks.openScreen((ServerPlayer) player, parent, parent.getBlockPos());
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.PROTOCOL_STASH_SIDE.get(), ProtocolStashSideBlockEntity::tick);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(ModItems.PROTOCOL_STASH_ITEM.get());
    }
}
