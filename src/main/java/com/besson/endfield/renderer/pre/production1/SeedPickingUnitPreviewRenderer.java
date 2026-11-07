package com.besson.endfield.renderer.pre.production1;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.SeedPickingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.SeedPickingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class SeedPickingUnitPreviewRenderer extends BasePreviewRenderer<SeedPickingUnitBlockEntity> {
    private static final SeedPickingUnitPreviewRenderer INSTANCE = new SeedPickingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.SEED_PICKING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.SEED_PICKING_UNIT.get().defaultBlockState().setValue(SeedPickingUnitBlock.FACING, facing).setValue(SeedPickingUnitBlock.PREVIEW, true);
    }

    @Override
    protected SeedPickingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new SeedPickingUnitBlockEntity(pos, state);
    }
}
