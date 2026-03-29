package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.custom.logicitis.ConvergerBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ConvergerBlockEntity extends BlockEntity {
    private int nextInputIndex = 0;
    public ConvergerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONVERGER.get(), pos, state);
    }

    public boolean tryMerge(Level world, BlockPos pos, Direction incomingDir, BeltBlockEntity sourceBelt) {

        BlockState state = world.getBlockState(pos);
        Direction outputDir = state.getValue(ConvergerBlock.FACING).getOpposite();

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
        BlockEntity outBE = world.getBlockEntity(outPos);

        if (!(outBE instanceof BeltBlockEntity targetBelt)) return false;
        if (!targetBelt.storedItem.isEmpty()) return false;

        // 成功转发
        targetBelt.storedItem = sourceBelt.storedItem;
        targetBelt.travelDirection = outputDir.getOpposite();

        sourceBelt.resetItem();

        // 轮询从当前成功方向的下一个开始
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
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("NextIndex", nextInputIndex);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        nextInputIndex = pTag.getInt("NextIndex");
    }
}
