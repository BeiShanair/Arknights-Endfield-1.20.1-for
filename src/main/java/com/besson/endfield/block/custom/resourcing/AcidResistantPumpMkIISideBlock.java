package com.besson.endfield.block.custom.resourcing;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.custom.resourcing.AcidResistantPumpMkIIBlockEntity;
import com.besson.endfield.blockEntity.custom.resourcing.AcidResistantPumpMkIISideBlockEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class AcidResistantPumpMkIISideBlock extends ModBlockEntityWithFacing {

    public AcidResistantPumpMkIISideBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AcidResistantPumpMkIISideBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof AcidResistantPumpMkIISideBlockEntity side) {
                AcidResistantPumpMkIIBlockEntity parent = side.getParentBlock();
                if (parent != null) {
                    NetworkHooks.openScreen((ServerPlayer) player, parent, parent.getBlockPos());
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AcidResistantPumpMkIISideBlockEntity side) {
                AcidResistantPumpMkIIBlockEntity parent = side.getParentBlock();
                if (parent != null) {
                    level.destroyBlock(parent.getBlockPos(), true);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(ModItems.ACID_RESISTANT_PUMP_MK_II_ITEM.get());
    }
}
