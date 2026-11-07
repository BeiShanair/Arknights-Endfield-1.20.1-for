package com.besson.endfield.block.custom.resourcing;

import com.besson.endfield.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class OriginiumDruseBlock extends Block {
    public OriginiumDruseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.5f) {
            BlockPos upPos = pos.above();
            if (level.getBlockState(upPos).isAir()) {
                level.setBlockAndUpdate(upPos, ModBlocks.ORIGINIUM_DRUSE.get().defaultBlockState());
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
