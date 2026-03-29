package com.besson.endfield.blockEntity.custom.logicitis;


import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BeltBridgeBlockEntity extends BlockEntity {
    public BeltBridgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BELT_BRIDGE.get(), pos, state);
    }

    public boolean tryPassThrough(Level world, BlockPos bridgePos, Direction incomingDir, BeltBlockEntity sourceBelt) {
        Direction outgoingDir = incomingDir;

        BlockPos outPos = bridgePos.relative(outgoingDir);
        BlockEntity outBE = world.getBlockEntity(outPos);

        if (!(outBE instanceof BeltBlockEntity targetBelt)) return false;

        if (!targetBelt.storedItem.isEmpty()) return false;

        // 直接转移
        targetBelt.storedItem = sourceBelt.storedItem;
        targetBelt.travelDirection = outgoingDir.getOpposite();

        sourceBelt.resetItem();

        return true;
    }
}
