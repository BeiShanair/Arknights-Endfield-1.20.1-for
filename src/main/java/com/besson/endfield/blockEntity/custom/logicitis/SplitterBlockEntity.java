package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.custom.logicitis.SplitterBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SplitterBlockEntity extends BlockEntity {
    private int nextIndex = 0;
    
    public SplitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPLITTER.get(), pos, state);
    }

    public boolean tryDistribute(Level world, BlockPos pos, Direction incomingDir, BeltBlockEntity sourceBelt) {

        BlockState state = world.getBlockState(pos);
        Direction inputDir = state.getValue(SplitterBlock.FACING);

        // 只允许从输入方向进入
        if (incomingDir.getOpposite() != inputDir) return false;

        Direction[] outputs = getOutputDirections(inputDir);

        for (int i = 0; i < outputs.length; i++) {

            Direction dir = outputs[(nextIndex + i) % outputs.length];

            BlockPos outPos = pos.relative(dir);
            BlockEntity outBE = world.getBlockEntity(outPos);

            if (!(outBE instanceof BeltBlockEntity targetBelt)) continue;
            if (!targetBelt.storedItem.isEmpty()) continue;

            // 转发
            targetBelt.storedItem = sourceBelt.storedItem;
            targetBelt.travelDirection = dir.getOpposite();

            sourceBelt.resetItem();

            // 更新轮询索引
            nextIndex = (nextIndex + 1) % outputs.length;

            return true;
        }

        return false; // 三个方向都被堵
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
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("NextIndex", nextIndex);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        nextIndex = pTag.getInt("NextIndex");
    }
}
