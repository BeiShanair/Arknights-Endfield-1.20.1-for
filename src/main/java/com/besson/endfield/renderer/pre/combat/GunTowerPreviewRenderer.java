package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.GunTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.GunTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class GunTowerPreviewRenderer extends BasePreviewRenderer<GunTowerBlockEntity> {
    private static final GunTowerPreviewRenderer INSTANCE = new GunTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.GUN_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.GUN_TOWER.get().defaultBlockState().setValue(GunTowerBlock.PREVIEW, true);
    }

    @Override
    protected GunTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new GunTowerBlockEntity(pos, state);
    }
}
