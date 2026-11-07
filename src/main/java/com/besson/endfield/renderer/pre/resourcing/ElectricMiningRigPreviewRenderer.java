package com.besson.endfield.renderer.pre.resourcing;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.resourcing.ElectricMiningRigBlock;
import com.besson.endfield.blockEntity.custom.resourcing.ElectricMiningRigBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ElectricMiningRigPreviewRenderer extends BasePreviewRenderer<ElectricMiningRigBlockEntity> {
    private static final ElectricMiningRigPreviewRenderer INSTANCE = new ElectricMiningRigPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.ELECTRIC_MINING_RIG.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.ELECTRIC_MINING_RIG.get().defaultBlockState().setValue(ElectricMiningRigBlock.FACING, facing).setValue(ElectricMiningRigBlock.PREVIEW, true);
    }

    @Override
    protected ElectricMiningRigBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new ElectricMiningRigBlockEntity(pos, state);
    }
}
