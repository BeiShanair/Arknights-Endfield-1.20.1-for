package com.besson.endfield.item.custom;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractMultiBlockItem extends BlockItem {

    protected AbstractMultiBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    
    protected abstract BlockPos[] getOccupiedPositions(BlockPlaceContext context);

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos[] occupied = this.getOccupiedPositions(context);
        if (!level.isClientSide() && !MultiBlockPlacementUtil.isAllReplaceable(level, occupied)) {
            Player player = context.getPlayer();
            if (player != null) {
                player.sendSystemMessage(Component.translatable("message.no_enough_area"));
            }
            return InteractionResult.FAIL;
        }

        InteractionResult result = super.place(context);
        if (result.consumesAction() && level instanceof ServerLevel serverLevel) {
            MultiBlockPlacementUtil.pushEntitiesOut(serverLevel, context.getClickedPos(), occupied);
        }
        return result;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return !this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos());
    }
}
