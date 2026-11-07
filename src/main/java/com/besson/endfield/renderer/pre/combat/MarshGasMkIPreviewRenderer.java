package com.besson.endfield.renderer.pre.combat;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.combat.MarshGasMkIBlock;
import com.besson.endfield.blockEntity.custom.combat.MarshGasMkIBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

public class MarshGasMkIPreviewRenderer extends BasePreviewRenderer<MarshGasMkIBlockEntity> {
    private static final MarshGasMkIPreviewRenderer INSTANCE = new MarshGasMkIPreviewRenderer();
    
    public static void register() {
        INSTANCE.start();
    }
    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.MARSH_GAS_MK_I.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        return ModBlocks.MARSH_GAS_MK_I.get().defaultBlockState().setValue(MarshGasMkIBlock.PREVIEW, true);
    }

    @Override
    protected MarshGasMkIBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new MarshGasMkIBlockEntity(pos, state);
    }
}
