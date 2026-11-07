package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.SentryTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.SentryTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class SentryTowerPreviewRenderer extends BasePreviewRenderer<SentryTowerBlockEntity> {
    private static final SentryTowerPreviewRenderer INSTANCE = new SentryTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.SENTRY_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.SENTRY_TOWER.get().defaultBlockState().setValue(SentryTowerBlock.PREVIEW, true);
    }

    @Override
    protected SentryTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new SentryTowerBlockEntity(pos, state);
    }
}
