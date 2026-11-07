package com.besson.endfield.renderer.pre.production1;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.FittingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.FittingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class FittingUnitPreviewRenderer extends BasePreviewRenderer<FittingUnitBlockEntity> {
    private static final FittingUnitPreviewRenderer INSTANCE = new FittingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.FITTING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.FITTING_UNIT.get().defaultBlockState().setValue(FittingUnitBlock.FACING, facing).setValue(FittingUnitBlock.PREVIEW, true);
    }

    @Override
    protected FittingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new FittingUnitBlockEntity(pos, state);
    }
}
