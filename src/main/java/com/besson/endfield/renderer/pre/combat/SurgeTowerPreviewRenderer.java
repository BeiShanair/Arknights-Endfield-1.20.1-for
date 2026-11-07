package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.SurgeTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.SurgeTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class SurgeTowerPreviewRenderer extends BasePreviewRenderer<SurgeTowerBlockEntity> {
    private static final SurgeTowerPreviewRenderer INSTANCE = new SurgeTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.SURGE_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.SURGE_TOWER.get().defaultBlockState().setValue(SurgeTowerBlock.PREVIEW, true);
    }

    @Override
    protected SurgeTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new SurgeTowerBlockEntity(pos, state);
    }
}
