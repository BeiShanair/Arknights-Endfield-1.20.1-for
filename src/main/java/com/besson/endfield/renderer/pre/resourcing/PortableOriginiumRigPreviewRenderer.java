package com.besson.endfield.renderer.pre.resourcing;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.resourcing.PortableOriginiumRigBlock;
import com.besson.endfield.blockEntity.custom.resourcing.PortableOriginiumRigBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PortableOriginiumRigPreviewRenderer extends BasePreviewRenderer<PortableOriginiumRigBlockEntity> {
    private static final PortableOriginiumRigPreviewRenderer INSTANCE = new PortableOriginiumRigPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.PORTABLE_ORIGINIUM_RIG.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.PORTABLE_ORIGINIUM_RIG.get().defaultBlockState().setValue(PortableOriginiumRigBlock.FACING, facing).setValue(PortableOriginiumRigBlock.PREVIEW, true);
    }

    @Override
    protected PortableOriginiumRigBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new PortableOriginiumRigBlockEntity(pos, state);
    }
}
