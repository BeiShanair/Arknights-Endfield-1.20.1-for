package com.besson.endfield.renderer.pre.powering;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.powering.ProtocolAnchorCorePortBlock;
import com.besson.endfield.blockEntity.custom.powering.ProtocolAnchorCoreBlockEntity;
import com.besson.endfield.renderer.pre.BasePreviewRenderer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ProtocolAnchorCorePreviewRenderer extends BasePreviewRenderer<ProtocolAnchorCoreBlockEntity> {
    private static final ProtocolAnchorCorePreviewRenderer INSTANCE = new ProtocolAnchorCorePreviewRenderer();

    public static void register() {
        INSTANCE.start();
    }

    @Override
    protected boolean matchesItem(ItemStack stack) {
        return stack.getItem() == ModBlocks.PROTOCOL_ANCHOR_CORE.get().asItem();
    }

    @Override
    protected BlockState getPreviewState(ItemStack stack, BlockHitResult bhr, Minecraft client) {
        Direction facing = client.player.getDirection().getOpposite();
        return ModBlocks.PROTOCOL_ANCHOR_CORE.get().defaultBlockState().setValue(ProtocolAnchorCorePortBlock.FACING, facing).setValue(ProtocolAnchorCorePortBlock.PREVIEW, true);
    }

    @Override
    protected ProtocolAnchorCoreBlockEntity createFakeBe(BlockPos pos, BlockState state) {
        return new ProtocolAnchorCoreBlockEntity(pos, state);
    }
}
