package com.besson.endfield.renderer.pre.powering;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.powering.RelayTowerBlock;
import com.besson.endfield.blockEntity.custom.powering.RelayTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class RelayTowerPreviewRenderer extends BasePreviewRenderer<RelayTowerBlockEntity> {
    private static final RelayTowerPreviewRenderer INSTANCE = new RelayTowerPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.RELAY_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.RELAY_TOWER.get().defaultBlockState().setValue(RelayTowerBlock.FACING, facing).setValue(RelayTowerBlock.PREVIEW, true);
    }

    @Override
    protected RelayTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new RelayTowerBlockEntity(pos, state);
    }
}
