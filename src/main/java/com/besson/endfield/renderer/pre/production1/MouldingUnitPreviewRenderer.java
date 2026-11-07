package com.besson.endfield.renderer.pre.production1;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.MouldingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.MouldingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class MouldingUnitPreviewRenderer extends BasePreviewRenderer<MouldingUnitBlockEntity> {
    private static final MouldingUnitPreviewRenderer INSTANCE = new MouldingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.MOULDING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.MOULDING_UNIT.get().defaultBlockState().setValue(MouldingUnitBlock.FACING, facing).setValue(MouldingUnitBlock.PREVIEW, true);
    }

    @Override
    protected MouldingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new MouldingUnitBlockEntity(pos, state);
    }
}
