package com.besson.endfield.renderer.pre.logicitis;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.depot.DepotLoaderBlock;
import com.besson.endfield.blockEntity.custom.depot.DepotLoaderBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class DepotLoaderPreviewRenderer extends BasePreviewRenderer<DepotLoaderBlockEntity> {
    private static final DepotLoaderPreviewRenderer INSTANCE = new DepotLoaderPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.DEPOT_LOADER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.DEPOT_LOADER.get().defaultBlockState().setValue(DepotLoaderBlock.FACING, facing).setValue(DepotLoaderBlock.PREVIEW, true);
    }

    @Override
    protected DepotLoaderBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new DepotLoaderBlockEntity(pos, state);
    }
}
