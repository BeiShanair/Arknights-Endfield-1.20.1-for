package com.besson.endfield.block.custom.powering;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.item.custom.ControlItem;
import com.besson.endfield.util.power.PowerNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BaseRelayBlock extends ModBlockEntityWithFacing {
    protected BaseRelayBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }
        if (player.getItemInHand(hand).getItem() instanceof ControlItem) {
            return super.use(state, level, pos, player, hand, hit);
        }

        PowerNetworkManager manager = PowerNetworkManager.get((ServerLevel) level);
        player.sendSystemMessage(Component.literal("----------------------"));
        player.sendSystemMessage(Component.translatable("electric_pylon.title"));
        player.sendSystemMessage(Component.translatable("electric_pylon.total_generated",
                manager.getLastTotalGenerated()).withStyle(ChatFormatting.GREEN));
        player.sendSystemMessage(Component.translatable("electric_pylon.total_demand",
                manager.getLastTotalDemand()).withStyle(ChatFormatting.RED));
        player.sendSystemMessage(Component.translatable("electric_pylon.stored_power",
                manager.getCurrentStoredEnergy()).withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.literal("----------------------"));
        return InteractionResult.SUCCESS;
    }
}
