package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.MedicalTowerBlock;
import com.besson.endfield.blockEntity.custom.combat.MedicalTowerBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class MedicalTowerPreviewRenderer extends BasePreviewRenderer<MedicalTowerBlockEntity> {
    private static final MedicalTowerPreviewRenderer INSTANCE = new MedicalTowerPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.MEDICAL_TOWER.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.MEDICAL_TOWER.get().defaultBlockState().setValue(MedicalTowerBlock.PREVIEW, true);
    }

    @Override
    protected MedicalTowerBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new MedicalTowerBlockEntity(pos, state);
    }
}
