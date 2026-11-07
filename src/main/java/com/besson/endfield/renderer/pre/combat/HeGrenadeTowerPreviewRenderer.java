package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.HeGrenadeTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.HeGrenadeTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class HeGrenadeTowerPreviewRenderer extends BasePreviewRenderer<HeGrenadeTowerBlockEntity> {
    private static final HeGrenadeTowerPreviewRenderer INSTANCE = new HeGrenadeTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.HE_GRENADE_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.HE_GRENADE_TOWER.get().defaultBlockState().setValue(HeGrenadeTowerBlock.PREVIEW, true);
    }

    @Override
    protected HeGrenadeTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new HeGrenadeTowerBlockEntity(pos, state);
    }
}
