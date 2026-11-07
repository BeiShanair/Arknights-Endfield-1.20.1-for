package com.besson.endfield.block.custom.production2;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.ExpandedCrucibleBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExpandedCrucibleBlock extends BaseCrucibleBlock {
    public ExpandedCrucibleBlock(Properties properties) {
        super(properties, ExpandedCrucibleBlockEntity.POWER_COST_PER_TICK, "6×5");
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExpandedCrucibleBlockEntity(pos, state);
    }

    @Override
    protected Block getSideBlock() {
        return ModBlocks.EXPANDED_CRUCIBLE_SIDE.get();
    }

    @Override
    protected BlockEntityType<? extends BaseCrucibleBlockEntity> crucibleType() {
        return ModBlockEntities.EXPANDED_CRUCIBLE.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("endfield.powerCost", 100).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("endfield.require_area", "6×5").withStyle(ChatFormatting.GRAY));
    }
}
