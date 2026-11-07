package com.besson.endfield.renderer.pre.logicitis;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.depot.DepotUnloaderBlock;
import com.besson.endfield.blockEntity.custom.depot.DepotUnloaderBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class DepotUnloaderPreviewRenderer extends BasePreviewRenderer<DepotUnloaderBlockEntity> {
    private static final DepotUnloaderPreviewRenderer INSTANCE = new DepotUnloaderPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.DEPOT_UNLOADER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.DEPOT_UNLOADER.get().defaultBlockState().setValue(DepotUnloaderBlock.FACING, facing).setValue(DepotUnloaderBlock.PREVIEW, true);
    }

    @Override
    protected DepotUnloaderBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new DepotUnloaderBlockEntity(pos, state);
    }
}
