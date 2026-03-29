package com.besson.endfield.block.custom.logicitis;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.logicitis.BeltBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

import java.util.List;

public class BeltBlock extends BaseEntityBlock {
    public static final EnumProperty<BeltShape> SHAPE = EnumProperty.create("belt_shape", BeltShape.class);
    protected static final VoxelShape STRAIGHT_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    protected static final VoxelShape ASCENDING_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    private boolean isStraight = false;
    public BeltBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, BeltShape.NORTH_SOUTH));
    }
    
    public static boolean isBelt(Level world, BlockPos pos) {
        return isBelt(world.getBlockState(pos));
    }

    public static boolean isBelt(BlockState state) {
        return state.getBlock() instanceof BeltBlock;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        BeltShape beltShape = state.is(this) ? state.getValue(this.getShapeProperty()) : null;
        return beltShape != null && beltShape.isAscending() ? ASCENDING_SHAPE : STRAIGHT_SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BeltBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.BELT.get(), BeltBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public Property<BeltShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                switch ((BeltShape)state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, BeltShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, BeltShape.NORTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch ((BeltShape)state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, BeltShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, BeltShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, BeltShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, BeltShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch ((BeltShape)state.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, BeltShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, BeltShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.setValue(SHAPE, BeltShape.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(SHAPE, BeltShape.NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        BeltShape beltShape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                switch (beltShape) {
                    case ASCENDING_NORTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, BeltShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, BeltShape.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_EAST);
                    default:
                        return super.mirror(state, mirror);
                }
            case FRONT_BACK:
                switch (beltShape) {
                    case ASCENDING_EAST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(SHAPE, BeltShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(SHAPE, BeltShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(SHAPE, BeltShape.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(SHAPE, BeltShape.NORTH_WEST);
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
        if (neighbor.defaultBlockState().isSignalSource() && new BeltPlacementHelper(world, pos, state).getNeighborCount() == 3) {
            this.updateBlockState(world, pos, state, false);
        }
    }

    protected BlockState updateBlockState(Level world, BlockPos pos, BlockState state, boolean forceUpdate) {
        if (world.isClientSide()) {
            return state;
        } else {
            BeltShape beltShape = state.getValue(this.getShapeProperty());
            return new BeltPlacementHelper(world, pos, state).updateBlockState(world.hasNeighborSignal(pos), forceUpdate, beltShape).getBlockState();
        }
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity b = world.getBlockEntity(pos);
            if (b instanceof BeltBlockEntity belt) {
                Containers.dropContents(world, pos, belt.getItem());
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
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
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        this.isStraight = ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown();
        BlockState blockState = super.defaultBlockState();
        Direction direction = ctx.getHorizontalDirection();
        boolean bl2 = direction == Direction.EAST || direction == Direction.WEST;
        return blockState.setValue(this.getShapeProperty(), bl2 ? BeltShape.EAST_WEST : BeltShape.NORTH_SOUTH);
    }

    public static Direction[] getConnections(BeltShape shape) {
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
    public static Direction getNextDirection(BeltShape shape, Direction incoming) {
        Direction[] connections = getConnections(shape);

        if (incoming == connections[0]) {
            return connections[1];
        }

        if (incoming == connections[1]) {
            return connections[0];
        }

        return null; // 不匹配说明不是合法输入
    }
    public static boolean isAscendingTowards(BeltShape shape, Direction direction) {
        return switch (shape) {
            case ASCENDING_EAST -> direction == Direction.EAST;
            case ASCENDING_WEST -> direction == Direction.WEST;
            case ASCENDING_NORTH -> direction == Direction.NORTH;
            case ASCENDING_SOUTH -> direction == Direction.SOUTH;
            default -> false;
        };
    }
    public static BlockPos getNextPos(BlockPos pos, BeltShape shape, Direction direction) {

        BlockPos next = pos.relative(direction);

        if (isAscendingTowards(shape, direction)) {
            next = next.above();
        } else if (isAscendingTowards(shape, direction.getOpposite())) {
            return next;
        }

        return next;
    }

    public boolean isFlexibleRail(BlockState state, Level world, BlockPos pos) {
        return !this.isStraight;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("belt.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
