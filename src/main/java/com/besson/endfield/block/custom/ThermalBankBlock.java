package com.besson.endfield.block.custom;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.ThermalBankBlockEntity;
import com.besson.endfield.blockEntity.custom.ThermalBankSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class ThermalBankBlock extends ModBlockEntityWithFacing {

    public ThermalBankBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ThermalBankBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.THERMAL_BANK.get(),
                (world1, pos, state1, blockEntity) ->
                        ThermalBankBlockEntity.tick(world1, pos, state1, (ThermalBankBlockEntity) blockEntity));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ThermalBankBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (ThermalBankBlockEntity) blockEntity, pPos);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide() && pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ThermalBankBlockEntity bank) {
                Containers.dropContents(pLevel, pPos, bank.getItems());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
            Direction facing = pState.getValue(FACING);
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();
            Direction backRight = back.getCounterClockWise();

            BlockPos[] positionsToCheck = {
                pPos.relative(right),
                pPos.relative(back),
                pPos.relative(back).relative(backRight)
            };
            for (BlockPos checkPos : positionsToCheck) {
                if (pLevel.getBlockState(checkPos).getBlock() == ModBlocks.THERMAL_BANK_SIDE.get()) {
                    pLevel.destroyBlock(checkPos, false);
                }
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            Direction facing = pState.getValue(FACING);
            Direction right = facing.getClockWise();
            Direction back = facing.getOpposite();
            Direction backRight = back.getCounterClockWise();

            BlockPos[] positionsToCheck = {
                pPos.relative(right),
                pPos.relative(back),
                pPos.relative(back).relative(backRight)
            };
            for (BlockPos checkPos : positionsToCheck) {
                pLevel.setBlockAndUpdate(checkPos, ModBlocks.THERMAL_BANK_SIDE.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)));
                BlockEntity entity = pLevel.getBlockEntity(checkPos);
                if (entity instanceof ThermalBankSideBlockEntity entity1) {
                    entity1.setParentPos(pPos);
                }
            }
        }
    }
}
