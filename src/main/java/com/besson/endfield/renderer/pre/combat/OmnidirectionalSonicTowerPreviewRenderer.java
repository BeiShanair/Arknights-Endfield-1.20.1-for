package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.OmnidirectionalSonicTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.OmnidirectionalSonicTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class OmnidirectionalSonicTowerPreviewRenderer extends BasePreviewRenderer<OmnidirectionalSonicTowerBlockEntity> {
    private static final OmnidirectionalSonicTowerPreviewRenderer INSTANCE = new OmnidirectionalSonicTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.OMNIDIRECTIONAL_SONIC_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.OMNIDIRECTIONAL_SONIC_TOWER.get().defaultBlockState().setValue(OmnidirectionalSonicTowerBlock.PREVIEW, true);
    }

    @Override
    protected OmnidirectionalSonicTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new OmnidirectionalSonicTowerBlockEntity(pos, state);
    }
}
