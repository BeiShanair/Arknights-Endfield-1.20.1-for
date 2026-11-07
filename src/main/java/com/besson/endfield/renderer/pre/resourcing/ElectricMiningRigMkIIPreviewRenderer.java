package com.besson.endfield.renderer.pre.resourcing;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.resourcing.ElectricMiningRigMkIIBlock;
import com.besson.endfield.blockEntity.custom.resourcing.ElectricMiningRigMkIIBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ElectricMiningRigMkIIPreviewRenderer extends BasePreviewRenderer<ElectricMiningRigMkIIBlockEntity> {
    private static final ElectricMiningRigMkIIPreviewRenderer INSTANCE = new ElectricMiningRigMkIIPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.ELECTRIC_MINING_RIG_MK_II.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.ELECTRIC_MINING_RIG_MK_II.get().defaultBlockState().setValue(ElectricMiningRigMkIIBlock.FACING, facing).setValue(ElectricMiningRigMkIIBlock.PREVIEW, true);
    }

    @Override
    protected ElectricMiningRigMkIIBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new ElectricMiningRigMkIIBlockEntity(pos, state);
    }
}
