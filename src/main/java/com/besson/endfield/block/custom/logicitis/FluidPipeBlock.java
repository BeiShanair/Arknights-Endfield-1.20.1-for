package com.besson.endfield.block.custom.logicitis;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.logicitis.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FluidPipeBlock extends BaseEntityBlock {
    public static final EnumProperty<FluidPipeShape> SHAPE = EnumProperty.create("fluid_pipe_shape", FluidPipeShape.class);
    protected static final VoxelShape STRAIGHT_SHAPE = Block.box(0.0, 5.0, 0.0, 16.0, 11.0, 16.0);
    protected static final VoxelShape ASCENDING_SHAPE = Block.box(0.0, 5.0, 0.0, 16.0, 11.0, 16.0);
    private boolean isStraight = false;

    public FluidPipeBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, FluidPipeShape.NORTH_SOUTH));
    }

    public static boolean isPipe(Level world, BlockPos pos) {
        return isPipe(world.getBlockState(pos));
    }

    public static boolean isPipe(BlockState state) {
        return state.getBlock() instanceof FluidPipeBlock;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        FluidPipeShape pipeShape = state.is(this) ? state.getValue(this.getShapeProperty()) : null;
        return pipeShape != null && pipeShape.isAscending() ? ASCENDING_SHAPE : STRAIGHT_SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidPipeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.FLUID_PIPE.get(), FluidPipeBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public Property<FluidPipeShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                switch ((FluidPipeShape) state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch ((FluidPipeShape) state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, FluidPipeShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch ((FluidPipeShape) state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, FluidPipeShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        FluidPipeShape pipeShape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                switch (pipeShape) {
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_EAST);
                    default:
                        return super.mirror(state, mirror);
                }
            case FRONT_BACK:
                switch (pipeShape) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, FluidPipeShape.NORTH_WEST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                }
        }
        return super.mirror(state, mirror);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            this.updateCurves(state, world, pos, notify);
        }
        this.isStraight = false;
    }

    protected BlockState updateCurves(BlockState state, Level world, BlockPos pos, boolean notify) {
        state = this.updateBlockState(world, pos, state, true);
        if (this.isStraight) {
            world.neighborChanged(state, pos, this, pos, notify);
        }
        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClientSide() && world.getBlockState(pos).is(this)) {
            this.updateBlockState(state, world, pos, sourceBlock);
        }
    }

    protected void updateBlockState(BlockState state, Level world, BlockPos pos, Block neighbor) {
        if (neighbor.defaultBlockState().isSignalSource() && new FluidPipePlacementHelper(world, pos, state).getNeighborCount() == 3) {
            this.updateBlockState(world, pos, state, false);
        }
    }

    protected BlockState updateBlockState(Level world, BlockPos pos, BlockState state, boolean forceUpdate) {
        if (world.isClientSide()) {
            return state;
        } else {
            FluidPipeShape pipeShape = state.getValue(this.getShapeProperty());
            return new FluidPipePlacementHelper(world, pos, state).updateBlockState(world.hasNeighborSignal(pos), forceUpdate, pipeShape).getBlockState();
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved) {
            if (state.getValue(getShapeProperty()).isAscending()) {
                world.updateNeighborsAt(pos.above(), this);
            }

            if (this.isStraight) {
                world.updateNeighborsAt(pos, this);
                world.updateNeighborsAt(pos.below(), this);
            }
        }

        super.onRemove(state, world, pos, newState, moved);
    }

    // TODO: 放置方法仍需修改，因为方块也是单例
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        this.isStraight = ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown();
        BlockState blockState = super.defaultBlockState();
        Direction direction = ctx.getHorizontalDirection();
        boolean bl = direction == Direction.EAST || direction == Direction.WEST;
        return blockState.setValue(this.getShapeProperty(), bl ? FluidPipeShape.EAST_WEST : FluidPipeShape.NORTH_SOUTH);
    }

    public static Direction[] getConnections(FluidPipeShape shape) {
        return switch (shape) {
            case NORTH_SOUTH -> new Direction[]{Direction.NORTH, Direction.SOUTH};
            case EAST_WEST -> new Direction[]{Direction.EAST, Direction.WEST};

            case SOUTH_EAST -> new Direction[]{Direction.SOUTH, Direction.EAST};
            case SOUTH_WEST -> new Direction[]{Direction.SOUTH, Direction.WEST};
            case NORTH_WEST -> new Direction[]{Direction.NORTH, Direction.WEST};
            case NORTH_EAST -> new Direction[]{Direction.NORTH, Direction.EAST};

            case ASCENDING_EAST -> new Direction[]{Direction.WEST, Direction.EAST};
            case ASCENDING_WEST -> new Direction[]{Direction.EAST, Direction.WEST};
            case ASCENDING_NORTH -> new Direction[]{Direction.SOUTH, Direction.NORTH};
            case ASCENDING_SOUTH -> new Direction[]{Direction.NORTH, Direction.SOUTH};
        };
    }

    public static Direction getNextDirection(FluidPipeShape shape, Direction incoming) {
        Direction[] connections = getConnections(shape);
        if (incoming == connections[0]) {
            return connections[1];
        }
        if (incoming == connections[1]) {
            return connections[0];
        }
        return null;
    }

    public static boolean isAscendingTowards(FluidPipeShape shape, Direction direction) {
        return switch (shape) {
            case ASCENDING_EAST -> direction == Direction.EAST;
            case ASCENDING_WEST -> direction == Direction.WEST;
            case ASCENDING_NORTH -> direction == Direction.NORTH;
            case ASCENDING_SOUTH -> direction == Direction.SOUTH;
            default -> false;
        };
    }

    public static BlockPos getNextPos(BlockPos pos, FluidPipeShape shape, Direction direction) {
        BlockPos next = pos.relative(direction);
        if (isAscendingTowards(shape, direction)) {
            next = next.above();
        } else if (isAscendingTowards(shape, direction.getOpposite())) {
            return next;
        }
        return next;
    }

    public boolean isFlexiblePipe(BlockState state, Level world, BlockPos pos) {
        return !this.isStraight;
    }
}
