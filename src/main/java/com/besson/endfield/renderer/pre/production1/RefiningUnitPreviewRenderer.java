package com.besson.endfield.renderer.pre.production1;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.RefiningUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.RefiningUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class RefiningUnitPreviewRenderer extends BasePreviewRenderer<RefiningUnitBlockEntity> {
    private static final RefiningUnitPreviewRenderer INSTANCE = new RefiningUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.REFINING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.REFINING_UNIT.get().defaultBlockState().setValue(RefiningUnitBlock.FACING, facing).setValue(RefiningUnitBlock.PREVIEW, true);
    }

    @Override
    protected RefiningUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new RefiningUnitBlockEntity(pos, state);
    }
}
