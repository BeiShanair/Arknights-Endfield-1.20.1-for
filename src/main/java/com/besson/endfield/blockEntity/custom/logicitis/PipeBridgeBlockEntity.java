package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PipeBridgeBlockEntity extends BlockEntity {
    public PipeBridgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_BRIDGE.get(), pos, state);
    }

    public boolean tryPassThrough(Level level, BlockPos bridgePos, Direction incomingDir, FluidPipeBlockEntity sourcePipe) {
        Direction outgoingDir = incomingDir;

        BlockPos outPos = bridgePos.relative(outgoingDir);
        BlockEntity outBE = level.getBlockEntity(outPos);

        if (!(outBE instanceof FluidPipeBlockEntity targetPipe)) return false;
        
        if (!targetPipe.canAcceptFrom(outgoingDir.getOpposite())) return false;
        
        targetPipe.acceptFluid(sourcePipe.storedFluid, sourcePipe.storedAmount, outgoingDir.getOpposite());

        sourcePipe.resetFluid();
        sourcePipe.setChanged();
        level.sendBlockUpdated(sourcePipe.getBlockPos(), sourcePipe.getBlockState(), sourcePipe.getBlockState(), 3);

        return true;
    }
}
