package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.custom.logicitis.PipeConvergerBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PipeConvergerBlockEntity extends BlockEntity {
    private int nextInputIndex = 0;

    public PipeConvergerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE_CONVERGER.get(), pos, state);
    }

    public boolean tryMerge(Level level, BlockPos pos, Direction incomingDir, FluidPipeBlockEntity sourcePipe) {

        BlockState state = level.getBlockState(pos);
        Direction outputDir = state.getValue(PipeConvergerBlock.FACING).getOpposite();

        if (incomingDir.getOpposite() == outputDir) return false;

        Direction[] inputs = getInputDirections(outputDir);

        int incomingIndex = -1;

        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == incomingDir.getOpposite()) {
                incomingIndex = i;
                break;
            }
        }

        if (incomingIndex == -1) return false;

        BlockPos outPos = pos.relative(outputDir);
        BlockEntity outBE = level.getBlockEntity(outPos);

        if (!(outBE instanceof FluidPipeBlockEntity targetPipe)) return false;
        if (!targetPipe.canAcceptFrom(outputDir.getOpposite())) return false;
        
        targetPipe.acceptFluid(sourcePipe.storedFluid, sourcePipe.storedAmount, outputDir.getOpposite());

        sourcePipe.resetFluid();
        sourcePipe.setChanged();
        level.sendBlockUpdated(sourcePipe.getBlockPos(), sourcePipe.getBlockState(), sourcePipe.getBlockState(), 3);
        
        nextInputIndex = (incomingIndex + 1) % inputs.length;

        return true;
    }

    private Direction[] getInputDirections(Direction outputDir) {
        Direction[] dirs = new Direction[3];
        int index = 0;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (dir != outputDir) {
                dirs[index++] = dir;
            }
        }
        return dirs;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("NextIndex", nextInputIndex);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        nextInputIndex = tag.getInt("NextIndex");
    }
}
