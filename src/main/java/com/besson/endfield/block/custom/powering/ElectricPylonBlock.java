package com.besson.endfield.block.custom.powering;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import com.besson.endfield.item.ModItems;
import com.besson.endfield.util.PowerNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ElectricPylonBlock extends ModBlockEntityWithFacing {

    public ElectricPylonBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElectricPylonBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.ELECTRIC_PYLON.get(), ElectricPylonBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
//            Item item = pPlayer.getItemInHand(pHand).getItem();
//            if (item instanceof ControlItem) return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
            PowerNetworkManager manager = PowerNetworkManager.get((ServerLevel) pLevel);
            if (pPlayer != null) {
                pPlayer.sendSystemMessage(Component.literal("----------------------"));
                pPlayer.sendSystemMessage(Component.translatable("electric_pylon.title"));
                pPlayer.sendSystemMessage(Component.translatable("electric_pylon.total_generated", manager.getLastTotalGenerated()).withStyle(ChatFormatting.GREEN));
                pPlayer.sendSystemMessage(Component.translatable("electric_pylon.total_demand", manager.getLastTotalDemand()).withStyle(ChatFormatting.RED));
                pPlayer.sendSystemMessage(Component.translatable("electric_pylon.stored_power", manager.getCurrentStoredEnergy()).withStyle(ChatFormatting.YELLOW));
                pPlayer.sendSystemMessage(Component.literal("----------------------"));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.CONSUME;
    }
}
