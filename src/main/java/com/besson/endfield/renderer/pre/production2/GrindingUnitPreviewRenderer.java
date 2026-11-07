package com.besson.endfield.renderer.pre.production2;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production2.GrindingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.GrindingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class GrindingUnitPreviewRenderer extends BasePreviewRenderer<GrindingUnitBlockEntity> {
    private static final GrindingUnitPreviewRenderer INSTANCE = new GrindingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.GRINDING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.GRINDING_UNIT.get().defaultBlockState().setValue(GrindingUnitBlock.FACING, facing).setValue(GrindingUnitBlock.PREVIEW, true);
    }

    @Override
    protected GrindingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new GrindingUnitBlockEntity(pos, state);
    }
}
