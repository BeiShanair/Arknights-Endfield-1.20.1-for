package com.besson.endfield.renderer.pre.logicitis;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.depot.ProtocolStashBlock;
import com.besson.endfield.blockEntity.custom.depot.ProtocolStashBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ProtocolStashPreviewRenderer extends BasePreviewRenderer<ProtocolStashBlockEntity> {
    private static final ProtocolStashPreviewRenderer INSTANCE = new ProtocolStashPreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.PROTOCOL_STASH.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.PROTOCOL_STASH.get().defaultBlockState().setValue(ProtocolStashBlock.FACING, facing).setValue(ProtocolStashBlock.PREVIEW, true);
    }

    @Override
    protected ProtocolStashBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new ProtocolStashBlockEntity(pos, state);
    }
}
