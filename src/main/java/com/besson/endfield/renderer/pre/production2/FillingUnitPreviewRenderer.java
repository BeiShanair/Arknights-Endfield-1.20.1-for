package com.besson.endfield.renderer.pre.production2;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production2.FillingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.FillingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class FillingUnitPreviewRenderer extends BasePreviewRenderer<FillingUnitBlockEntity> {
    private static final FillingUnitPreviewRenderer INSTANCE = new FillingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.FILLING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.FILLING_UNIT.get().defaultBlockState().setValue(FillingUnitBlock.FACING, facing).setValue(FillingUnitBlock.PREVIEW, true);
    }

    @Override
    protected FillingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new FillingUnitBlockEntity(pos, state);
    }
}
