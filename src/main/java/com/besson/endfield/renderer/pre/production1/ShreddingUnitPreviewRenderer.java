package com.besson.endfield.renderer.pre.production1;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.ShreddingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.ShreddingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ShreddingUnitPreviewRenderer extends BasePreviewRenderer<ShreddingUnitBlockEntity> {
    private static final ShreddingUnitPreviewRenderer INSTANCE = new ShreddingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.SHREDDING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.SHREDDING_UNIT.get().defaultBlockState().setValue(ShreddingUnitBlock.FACING, facing).setValue(ShreddingUnitBlock.PREVIEW, true);
    }

    @Override
    protected ShreddingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new ShreddingUnitBlockEntity(pos, state);
    }
}
