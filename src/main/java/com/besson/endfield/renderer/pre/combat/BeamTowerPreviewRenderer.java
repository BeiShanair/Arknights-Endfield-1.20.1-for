package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.BeamTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.BeamTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class BeamTowerPreviewRenderer extends BasePreviewRenderer<BeamTowerBlockEntity> {
    private static final BeamTowerPreviewRenderer INSTANCE = new BeamTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.BEAM_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.BEAM_TOWER.get().defaultBlockState().setValue(BeamTowerBlock.PREVIEW, true);
    }

    @Override
    protected BeamTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new BeamTowerBlockEntity(pos, state);
    }
}
