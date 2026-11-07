package com.besson.endfield.renderer.pre.production2;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production2.GearingUnitBlock;
import com.besson.endfield.blockEntity.custom.production2.GearingUnitBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class GearingUnitPreviewRenderer extends BasePreviewRenderer<GearingUnitBlockEntity> {
    private static final GearingUnitPreviewRenderer INSTANCE = new GearingUnitPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.GEARING_UNIT.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.GEARING_UNIT.get().defaultBlockState().setValue(GearingUnitBlock.FACING, facing).setValue(GearingUnitBlock.PREVIEW, true);
    }

    @Override
    protected GearingUnitBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new GearingUnitBlockEntity(pos, state);
    }
}
