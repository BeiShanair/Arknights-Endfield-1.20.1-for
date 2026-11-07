package com.besson.endfield.renderer.pre.powering;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.powering.ElectricPylonBlock;
import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ElectricPylonPreviewRenderer extends BasePreviewRenderer<ElectricPylonBlockEntity> {
    private static final ElectricPylonPreviewRenderer INSTANCE = new ElectricPylonPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.ELECTRIC_PYLON.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.ELECTRIC_PYLON.get().defaultBlockState().setValue(ElectricPylonBlock.FACING, facing).setValue(ElectricPylonBlock.PREVIEW, true);
    }

    @Override
    protected ElectricPylonBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new ElectricPylonBlockEntity(pos, state);
    }
}
