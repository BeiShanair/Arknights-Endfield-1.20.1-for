package com.besson.endfield.renderer.pre.production1;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.PlantingUnitBlock;
import com.besson.endfield.blockEntity.custom.production1.PlantingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PlantingUnitPreviewRenderer extends BasePreviewRenderer<PlantingUnitBlockEntity> {
    private static final PlantingUnitPreviewRenderer INSTANCE = new PlantingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.PLANTING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.PLANTING_UNIT.get().defaultBlockState().setValue(PlantingUnitBlock.FACING, facing).setValue(PlantingUnitBlock.PREVIEW, true);
    }

    @Override
    protected PlantingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new PlantingUnitBlockEntity(pos, state);
    }
}
