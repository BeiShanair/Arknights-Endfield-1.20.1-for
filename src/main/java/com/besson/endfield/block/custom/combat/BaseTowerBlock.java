package com.besson.endfield.block.custom.combat;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BaseTowerBlock extends BaseEntityBlock {
    public static final BooleanProperty PREVIEW = ModBlockEntityWithFacing.PREVIEW;
    protected BaseTowerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PREVIEW, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PREVIEW);
    }
}
