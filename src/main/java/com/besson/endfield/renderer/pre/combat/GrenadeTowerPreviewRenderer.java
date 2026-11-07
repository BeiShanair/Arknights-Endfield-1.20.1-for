package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.GrenadeTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.GrenadeTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class GrenadeTowerPreviewRenderer extends BasePreviewRenderer<GrenadeTowerBlockEntity> {
    private static final GrenadeTowerPreviewRenderer INSTANCE = new GrenadeTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.GRENADE_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.GRENADE_TOWER.get().defaultBlockState().setValue(GrenadeTowerBlock.PREVIEW, true);
    }

    @Override
    protected GrenadeTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new GrenadeTowerBlockEntity(pos, state);
    }
}
