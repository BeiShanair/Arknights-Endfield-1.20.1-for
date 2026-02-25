package com.besson.endfield.block.custom;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeltPlacementHelper {
    private final Level world;
    private final BlockPos pos;
    private final BeltBlock block;
    private BlockState state;
    private final boolean forbidCurves;
    private final List<BlockPos> neighbors = Lists.<BlockPos>newArrayList();

    public BeltPlacementHelper(Level world, BlockPos pos, BlockState state) {
        this.world = world;
        this.pos = pos;
        this.state = state;
        this.block = (BeltBlock)state.getBlock();
        BeltShape beltShape = state.getValue(this.block.getShapeProperty());
        this.forbidCurves = false;
        this.computeNeighbors(beltShape);
    }
    public List<BlockPos> getNeighbors() {
        return this.neighbors;
    }

    private void computeNeighbors(BeltShape shape) {
        this.neighbors.clear();
        switch (shape) {
            case NORTH_SOUTH:
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south());
                break;
            case EAST_WEST:
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.east());
                break;
            case ASCENDING_EAST:
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.east().above());
                break;
            case ASCENDING_WEST:
                this.neighbors.add(this.pos.west().above());
                this.neighbors.add(this.pos.east());
                break;
            case ASCENDING_NORTH:
                this.neighbors.add(this.pos.north().above());
                this.neighbors.add(this.pos.south());
                break;
            case ASCENDING_SOUTH:
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().above());
                break;
            case SOUTH_EAST:
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south());
                break;
            case SOUTH_WEST:
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.south());
                break;
            case NORTH_WEST:
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north());
                break;
            case NORTH_EAST:
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.north());
        }
    }

    private void updateNeighborPositions() {
        for (int i = 0; i < this.neighbors.size(); i++) {
            BeltPlacementHelper beltPlacementHelper = this.getNeighboringRail((BlockPos)this.neighbors.get(i));
            if (beltPlacementHelper != null && beltPlacementHelper.isNeighbor(this)) {
                this.neighbors.set(i, beltPlacementHelper.pos);
            } else {
                this.neighbors.remove(i--);
            }
        }
    }

    private boolean isVerticallyNearRail(BlockPos pos) {
        return BeltBlock.isBelt(this.world, pos) || BeltBlock.isBelt(this.world, pos.above()) || BeltBlock.isBelt(this.world, pos.below());
    }

    @Nullable
    private BeltPlacementHelper getNeighboringRail(BlockPos pos) {
        BlockState blockState = this.world.getBlockState(pos);
        if (BeltBlock.isBelt(blockState)) {
            return new BeltPlacementHelper(this.world, pos, blockState);
        } else {
            BlockPos blockPos = pos.above();
            blockState = this.world.getBlockState(blockPos);
            if (BeltBlock.isBelt(blockState)) {
                return new BeltPlacementHelper(this.world, blockPos, blockState);
            } else {
                blockPos = pos.below();
                blockState = this.world.getBlockState(blockPos);
                return BeltBlock.isBelt(blockState) ? new BeltPlacementHelper(this.world, blockPos, blockState) : null;
            }
        }
    }

    private boolean isNeighbor(BeltPlacementHelper other) {
        return this.isNeighbor(other.pos);
    }

    private boolean isNeighbor(BlockPos pos) {
        for (int i = 0; i < this.neighbors.size(); i++) {
            BlockPos blockPos = (BlockPos)this.neighbors.get(i);
            if (blockPos.getX() == pos.getX() && blockPos.getZ() == pos.getZ()) {
                return true;
            }
        }

        return false;
    }

    protected int getNeighborCount() {
        int i = 0;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.isVerticallyNearRail(this.pos.relative(direction))) {
                i++;
            }
        }

        return i;
    }

    private boolean canConnect(BeltPlacementHelper placementHelper) {
        return this.isNeighbor(placementHelper) || this.neighbors.size() != 2;
    }

    private void computeBeltShape(BeltPlacementHelper placementHelper) {
        this.neighbors.add(placementHelper.pos);
        BlockPos blockPos = this.pos.north();
        BlockPos blockPos2 = this.pos.south();
        BlockPos blockPos3 = this.pos.west();
        BlockPos blockPos4 = this.pos.east();
        boolean bl = this.isNeighbor(blockPos);
        boolean bl2 = this.isNeighbor(blockPos2);
        boolean bl3 = this.isNeighbor(blockPos3);
        boolean bl4 = this.isNeighbor(blockPos4);
        BeltShape railShape = null;
        if (bl || bl2) {
            railShape = BeltShape.NORTH_SOUTH;
        }

        if (bl3 || bl4) {
            railShape = BeltShape.EAST_WEST;
        }

        if (!this.forbidCurves) {
            if (bl2 && bl4 && !bl && !bl3) {
                railShape = BeltShape.SOUTH_EAST;
            }

            if (bl2 && bl3 && !bl && !bl4) {
                railShape = BeltShape.SOUTH_WEST;
            }

            if (bl && bl3 && !bl2 && !bl4) {
                railShape = BeltShape.NORTH_WEST;
            }

            if (bl && bl4 && !bl2 && !bl3) {
                railShape = BeltShape.NORTH_EAST;
            }
        }

        if (railShape == BeltShape.NORTH_SOUTH) {
            if (BeltBlock.isBelt(this.world, blockPos.above())) {
                railShape = BeltShape.ASCENDING_NORTH;
            }

            if (BeltBlock.isBelt(this.world, blockPos2.above())) {
                railShape = BeltShape.ASCENDING_SOUTH;
            }
        }

        if (railShape == BeltShape.EAST_WEST) {
            if (BeltBlock.isBelt(this.world, blockPos4.above())) {
                railShape = BeltShape.ASCENDING_EAST;
            }

            if (BeltBlock.isBelt(this.world, blockPos3.above())) {
                railShape = BeltShape.ASCENDING_WEST;
            }
        }

        if (railShape == null) {
            railShape = BeltShape.NORTH_SOUTH;
        }

        this.state = this.state.setValue(this.block.getShapeProperty(), railShape);
        this.world.setBlock(this.pos, this.state, 3);
    }

    private boolean canConnect(BlockPos pos) {
        BeltPlacementHelper railPlacementHelper = this.getNeighboringRail(pos);
        if (railPlacementHelper == null) {
            return false;
        } else {
            railPlacementHelper.updateNeighborPositions();
            return railPlacementHelper.canConnect(this);
        }
    }

    public BeltPlacementHelper updateBlockState(boolean powered, boolean forceUpdate, BeltShape railShape) {
        BlockPos blockPos = this.pos.north();
        BlockPos blockPos2 = this.pos.south();
        BlockPos blockPos3 = this.pos.west();
        BlockPos blockPos4 = this.pos.east();
        boolean bl = this.canConnect(blockPos);
        boolean bl2 = this.canConnect(blockPos2);
        boolean bl3 = this.canConnect(blockPos3);
        boolean bl4 = this.canConnect(blockPos4);
        BeltShape railShape2 = null;
        boolean bl5 = bl || bl2;
        boolean bl6 = bl3 || bl4;
        if (bl5 && !bl6) {
            railShape2 = BeltShape.NORTH_SOUTH;
        }

        if (bl6 && !bl5) {
            railShape2 = BeltShape.EAST_WEST;
        }

        boolean bl7 = bl2 && bl4;
        boolean bl8 = bl2 && bl3;
        boolean bl9 = bl && bl4;
        boolean bl10 = bl && bl3;
        if (!this.forbidCurves) {
            if (bl7 && !bl && !bl3) {
                railShape2 = BeltShape.SOUTH_EAST;
            }

            if (bl8 && !bl && !bl4) {
                railShape2 = BeltShape.SOUTH_WEST;
            }

            if (bl10 && !bl2 && !bl4) {
                railShape2 = BeltShape.NORTH_WEST;
            }

            if (bl9 && !bl2 && !bl3) {
                railShape2 = BeltShape.NORTH_EAST;
            }
        }

        if (railShape2 == null) {
            if (bl5 && bl6) {
                railShape2 = railShape;
            } else if (bl5) {
                railShape2 = BeltShape.NORTH_SOUTH;
            } else if (bl6) {
                railShape2 = BeltShape.EAST_WEST;
            }

            if (!this.forbidCurves) {
                if (powered) {
                    if (bl7) {
                        railShape2 = BeltShape.SOUTH_EAST;
                    }

                    if (bl8) {
                        railShape2 = BeltShape.SOUTH_WEST;
                    }

                    if (bl9) {
                        railShape2 = BeltShape.NORTH_EAST;
                    }

                    if (bl10) {
                        railShape2 = BeltShape.NORTH_WEST;
                    }
                } else {
                    if (bl10) {
                        railShape2 = BeltShape.NORTH_WEST;
                    }

                    if (bl9) {
                        railShape2 = BeltShape.NORTH_EAST;
                    }

                    if (bl8) {
                        railShape2 = BeltShape.SOUTH_WEST;
                    }

                    if (bl7) {
                        railShape2 = BeltShape.SOUTH_EAST;
                    }
                }
            }
        }

        if (railShape2 == BeltShape.NORTH_SOUTH) {
            if (BeltBlock.isBelt(this.world, blockPos.above())) {
                railShape2 = BeltShape.ASCENDING_NORTH;
            }

            if (BeltBlock.isBelt(this.world, blockPos2.above())) {
                railShape2 = BeltShape.ASCENDING_SOUTH;
            }
        }

        if (railShape2 == BeltShape.EAST_WEST) {
            if (BeltBlock.isBelt(this.world, blockPos4.above())) {
                railShape2 = BeltShape.ASCENDING_EAST;
            }

            if (BeltBlock.isBelt(this.world, blockPos3.above())) {
                railShape2 = BeltShape.ASCENDING_WEST;
            }
        }

        if (railShape2 == null) {
            railShape2 = railShape;
        }

        this.computeNeighbors(railShape2);
        this.state = this.state.setValue(this.block.getShapeProperty(), railShape2);
        if (forceUpdate || this.world.getBlockState(this.pos) != this.state) {
            this.world.setBlock(this.pos, this.state, 3);

            for (int i = 0; i < this.neighbors.size(); i++) {
                BeltPlacementHelper beltPlacementHelper = this.getNeighboringRail((BlockPos)this.neighbors.get(i));
                if (beltPlacementHelper != null) {
                    beltPlacementHelper.updateNeighborPositions();
                    if (beltPlacementHelper.canConnect(this)) {
                        beltPlacementHelper.computeBeltShape(this);
                    }
                }
            }
        }

        return this;
    }

    public BlockState getBlockState() {
        return this.state;
    }
}
