package com.besson.endfield.renderer.pre.powering;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.powering.ThermalBankBlock;
import com.besson.endfield.blockEntity.custom.powering.ThermalBankBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ThermalBankPreviewRenderer extends BasePreviewRenderer<ThermalBankBlockEntity> {
    private static final ThermalBankPreviewRenderer INSTANCE = new ThermalBankPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.THERMAL_BANK.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.THERMAL_BANK.get().defaultBlockState().setValue(ThermalBankBlock.FACING, facing).setValue(ThermalBankBlock.PREVIEW, true);
    }

    @Override
    protected ThermalBankBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new ThermalBankBlockEntity(pos, state);
    }
}
