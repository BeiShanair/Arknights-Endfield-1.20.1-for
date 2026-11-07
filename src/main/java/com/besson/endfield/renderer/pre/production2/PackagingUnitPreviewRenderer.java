package com.besson.endfield.renderer.pre.production2;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production2.PackagingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.PackagingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PackagingUnitPreviewRenderer extends BasePreviewRenderer<PackagingUnitBlockEntity> {
    private static final PackagingUnitPreviewRenderer INSTANCE = new PackagingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.PACKAGING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.PACKAGING_UNIT.get().defaultBlockState().setValue(PackagingUnitBlock.FACING, facing).setValue(PackagingUnitBlock.PREVIEW, true);
    }

    @Override
    protected PackagingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new PackagingUnitBlockEntity(pos, state);
    }
}
