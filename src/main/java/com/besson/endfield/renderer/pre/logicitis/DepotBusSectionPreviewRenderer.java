package com.besson.endfield.renderer.pre.logicitis;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.depot.DepotBusSectionBlock;
import com.besson.endfield.blockEntity.custom.depot.DepotBusSectionBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class DepotBusSectionPreviewRenderer extends BasePreviewRenderer<DepotBusSectionBlockEntity> {
    private static final DepotBusSectionPreviewRenderer INSTANCE = new DepotBusSectionPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.DEPOT_BUS_SECTION.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.DEPOT_BUS_SECTION.get().defaultBlockState().setValue(DepotBusSectionBlock.FACING, facing).setValue(DepotBusSectionBlock.PREVIEW, true);
    }

    @Override
    protected DepotBusSectionBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new DepotBusSectionBlockEntity(pos, state);
    }
}
