package com.besson.endfield.block.custom.production2;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public abstract class BaseCrucibleSideBlock extends ModBlockEntityWithFacing {
    protected BaseCrucibleSideBlock(Properties properties) {
        super(properties);
    }
    
    protected abstract BlockEntityType<? extends BaseCrucibleSideBlockEntity> sideType();
    
    protected abstract Item getPickItem();

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BaseCrucibleSideBlockEntity sideBlockEntity) {
                BaseCrucibleBlockEntity parent = sideBlockEntity.getParentBlock();
                if (parent != null) {
                    level.destroyBlock(parent.getBlockPos(), true);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof BaseCrucibleSideBlockEntity sideEntity && player instanceof ServerPlayer serverPlayer) {
                BaseCrucibleBlockEntity parent = sideEntity.getParentBlock();
                if (parent != null) {
                    NetworkHooks.openScreen(serverPlayer, parent, parent.getBlockPos());
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, sideType(), BaseCrucibleSideBlockEntity::tick);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(getPickItem());
    }
}
