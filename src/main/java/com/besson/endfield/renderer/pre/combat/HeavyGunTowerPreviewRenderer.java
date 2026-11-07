package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.HeavyGunTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.HeavyGunTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class HeavyGunTowerPreviewRenderer extends BasePreviewRenderer<HeavyGunTowerBlockEntity> {
    private static final HeavyGunTowerPreviewRenderer INSTANCE = new HeavyGunTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.HEAVY_GUN_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.HEAVY_GUN_TOWER.get().defaultBlockState().setValue(HeavyGunTowerBlock.PREVIEW, true);
    }

    @Override
    protected HeavyGunTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new HeavyGunTowerBlockEntity(pos, state);
    }
}
