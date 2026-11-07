package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.LNTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.LNTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class LNTowerPreviewRenderer extends BasePreviewRenderer<LNTowerBlockEntity> {
    private static final LNTowerPreviewRenderer INSTANCE = new LNTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.LN_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.LN_TOWER.get().defaultBlockState().setValue(LNTowerBlock.PREVIEW, true);
    }

    @Override
    protected LNTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new LNTowerBlockEntity(pos, state);
    }
}
