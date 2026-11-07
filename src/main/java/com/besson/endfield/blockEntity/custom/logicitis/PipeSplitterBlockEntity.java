package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.custom.logicitis.PipeSplitterBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PipeSplitterBlockEntity extends BlockEntity {
    private int nextIndex = 0;

    public PipeSplitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_SPLITTER.get(), pos, state);
    }

    public boolean tryDistribute(Level level, BlockPos pos, Direction incomingDir, FluidPipeBlockEntity sourcePipe) {

        BlockState state = level.getBlockState(pos);
        Direction inputDir = state.getValue(PipeSplitterBlock.FACING);
        
        if (incomingDir.getOpposite() != inputDir) return false;

        Direction[] outputs = getOutputDirections(inputDir);

        for (int i = 0; i < outputs.length; i++) {

            Direction dir = outputs[(nextIndex + i) % outputs.length];

            BlockPos outPos = pos.relative(dir);
            BlockEntity outBE = level.getBlockEntity(outPos);

            if (!(outBE instanceof FluidPipeBlockEntity targetPipe)) continue;
            if (!targetPipe.canAcceptFrom(dir.getOpposite())) continue;
            
            targetPipe.acceptFluid(sourcePipe.storedFluid, sourcePipe.storedAmount, dir.getOpposite());

            sourcePipe.resetFluid();
            sourcePipe.setChanged();
            level.sendBlockUpdated(sourcePipe.getBlockPos(), sourcePipe.getBlockState(), sourcePipe.getBlockState(), 3);
            
            nextIndex = (nextIndex + 1) % outputs.length;

            return true;
        }

        return false;
    }

    private Direction[] getOutputDirections(Direction inputDir) {

        Direction[] dirs = new Direction[3];
        int index = 0;

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (dir != inputDir) {
                dirs[index++] = dir;
            }
        }

        return dirs;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("NextIndex", nextIndex);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        nextIndex = tag.getInt("NextIndex");
    }
}
