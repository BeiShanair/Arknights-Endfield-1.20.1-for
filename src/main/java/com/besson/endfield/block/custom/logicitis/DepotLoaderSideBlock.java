package com.besson.endfield.block.custom.logicitis;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.custom.logicitis.DepotLoaderBlockEntity;
import com.besson.endfield.blockEntity.custom.logicitis.DepotLoaderSideBlockEntity;
import com.besson.endfield.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class DepotLoaderSideBlock extends ModBlockEntityWithFacing {
    public DepotLoaderSideBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DepotLoaderSideBlockEntity(pos, state);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DepotLoaderSideBlockEntity sideBlockEntity) {
                DepotLoaderBlockEntity parent = sideBlockEntity.getParentBlock();
                if (parent != null) {
                    BlockPos parentPos = parent.getBlockPos();
                    world.destroyBlock(parentPos, true);
                }
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(ModItems.DEPOT_LOADER_ITEM.get());
    }
}
