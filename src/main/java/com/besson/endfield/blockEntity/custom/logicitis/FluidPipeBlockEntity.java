package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.custom.logicitis.FluidPipeBlock;
import com.besson.endfield.block.custom.logicitis.FluidPipeShape;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class FluidPipeBlockEntity extends BlockEntity {
    public static final int CAPACITY = 2000;
    public FluidStack storedFluid = FluidStack.EMPTY;
    public int storedAmount = 0;
    public Direction travelDirection = null;
    public PipeMode mode = PipeMode.TRANSFER;
    private int tickCounter = 0;
    
    private final Map<Direction, LazyOptional<IFluidHandler>> sidedHandlers = new EnumMap<>(Direction.class);

    public FluidPipeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_PIPE.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side != null && level != null) {
            BlockState state = level.getBlockState(worldPosition);
            if (state.getBlock() instanceof FluidPipeBlock && isConnectedEnd(state, side)) {
                return sidedHandlers
                        .computeIfAbsent(side, s -> LazyOptional.of(() -> new PipeFluidHandler(s)))
                        .cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        sidedHandlers.values().forEach(LazyOptional::invalidate);
        sidedHandlers.clear();
    }
    
    private boolean isConnectedEnd(BlockState state, Direction side) {
        Direction[] connections = FluidPipeBlock.getConnections(state.getValue(FluidPipeBlock.SHAPE));
        return side == connections[0] || side == connections[1];
    }
    
    private boolean isOutputFace(BlockState state, Direction side) {
        if (travelDirection == null) return false;
        FluidPipeShape shape = state.getValue(FluidPipeBlock.SHAPE);
        return FluidPipeBlock.getNextDirection(shape, travelDirection) == side;
    }

    private BlockState pipeState() {
        return level == null ? getBlockState() : level.getBlockState(worldPosition);
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            BlockState state = pipeState();
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }
    }
    
    public boolean isEmptyPipe() {
        return storedFluid.isEmpty() || storedAmount <= 0;
    }

    public FluidStack getStoredStack() {
        if (isEmptyPipe()) return FluidStack.EMPTY;
        FluidStack stack = storedFluid.copy();
        stack.setAmount(storedAmount);
        return stack;
    }

    public void resetFluid() {
        this.storedFluid = FluidStack.EMPTY;
        this.storedAmount = 0;
        this.travelDirection = null;
    }

    public boolean canAcceptFrom(Direction from) {
        if (!isEmptyPipe()) return false;
        return isConnectedEnd(pipeState(), from);
    }

    public void acceptFluid(FluidStack fluid, int amount, Direction from) {
        if (!isEmptyPipe()) return;
        int accepted = Math.min(amount, CAPACITY);
        if (accepted <= 0) return;

        this.storedFluid = fluid.copy();
        this.storedFluid.setAmount(accepted);
        this.storedAmount = accepted;
        this.travelDirection = from;

        sync();
    }
    
    public void acceptFluid(Direction from) {
        if (!isEmptyPipe()) return;
        if (this.travelDirection != null) return;
        this.travelDirection = from;
        setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity be) {
        if (level.isClientSide()) return;

        be.tickCounter++;
        if (be.tickCounter < 20) return;
        be.tickCounter = 0;
        
        BlockState currentState = level.getBlockState(pos);
        
        if (be.mode == PipeMode.EXTRACT && be.isEmptyPipe()) {
            be.tryExtractFromNeighbor(level, pos, currentState);
        }

        if (be.isEmptyPipe()) return;
        if (be.travelDirection == null) return;
        
        int pushResult = be.tryPushToStorage(level, pos, currentState);
        if (pushResult == 1) {
            be.resetFluid();
            be.sync();
            return;
        } else if (pushResult == 0) {
            
            be.sync();
            return;
        }

        if (be.tryPushForward(level, pos, currentState)) {
            be.resetFluid();
        }
        be.sync();
    }
    
    private void tryExtractFromNeighbor(Level level, BlockPos pos, BlockState state) {
        FluidPipeShape shape = state.getValue(FluidPipeBlock.SHAPE);
        for (Direction side : FluidPipeBlock.getConnections(shape)) {
            BlockPos neighborPos = pos.relative(side);
            if (level.getBlockEntity(neighborPos) instanceof FluidPipeBlockEntity) continue;

            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            if (neighbor == null) continue;

            IFluidHandler handler = neighbor
                    .getCapability(ForgeCapabilities.FLUID_HANDLER, side.getOpposite())
                    .orElse(null);
            if (handler == null) continue;

            FluidStack drained = handler.drain(CAPACITY, IFluidHandler.FluidAction.EXECUTE);
            if (drained.isEmpty()) continue;

            this.storedFluid = drained.copy();
            this.storedFluid.setAmount(drained.getAmount());
            this.storedAmount = drained.getAmount();
            this.travelDirection = side;
            sync();
            return;
        }
    }
    
    private int tryPushToStorage(Level level, BlockPos pos, BlockState state) {
        if (this.mode != PipeMode.INSERT) return -1;
        if (isEmptyPipe()) return -1;

        FluidPipeShape shape = state.getValue(FluidPipeBlock.SHAPE);
        Direction next = FluidPipeBlock.getNextDirection(shape, this.travelDirection);
        if (next == null) return -1;

        BlockPos outputPos = FluidPipeBlock.getNextPos(pos, shape, next);
        BlockEntity targetBe = level.getBlockEntity(outputPos);

        if (targetBe == null || targetBe instanceof FluidPipeBlockEntity) return -1;

        IFluidHandler handler = targetBe
                .getCapability(ForgeCapabilities.FLUID_HANDLER, next.getOpposite())
                .orElse(null);
        if (handler == null) return -1;

        int inserted = handler.fill(getStoredStack(), IFluidHandler.FluidAction.EXECUTE);
        if (inserted <= 0) return 0;

        this.storedAmount -= inserted;
        if (this.storedAmount <= 0) {
            resetFluid();
        } else {
            this.storedFluid.setAmount(this.storedAmount);
        }
        sync();
        return 1;
    }
    
    private boolean tryPushForward(Level level, BlockPos pos, BlockState state) {
        FluidPipeShape shape = state.getValue(FluidPipeBlock.SHAPE);

        Direction next = FluidPipeBlock.getNextDirection(shape, this.travelDirection);
        if (next == null) return false;

        BlockPos forwardPos = FluidPipeBlock.getNextPos(pos, shape, next);
        Direction entryFace = next.getOpposite();
        FluidStack slug = getStoredStack();

        BlockEntity forwardBe = level.getBlockEntity(forwardPos);
        
        if (forwardBe instanceof PipeBridgeBlockEntity bridge) {
            return bridge.tryPassThrough(level, forwardPos, next, this);
        }
        if (forwardBe instanceof PipeSplitterBlockEntity splitter) {
            return splitter.tryDistribute(level, forwardPos, next, this);
        }
        if (forwardBe instanceof PipeConvergerBlockEntity converger) {
            return converger.tryMerge(level, forwardPos, next, this);
        }

        if (forwardBe instanceof FluidPipeBlockEntity forwardPipe) {
            if (forwardPipe.canAcceptFrom(entryFace)) {
                forwardPipe.acceptFluid(slug, this.storedAmount, entryFace);
                return true;
            }
            return false;
        }
        
        if (level.getBlockEntity(forwardPos.below()) instanceof FluidPipeBlockEntity belowPipe
                && belowPipe.canAcceptFrom(entryFace)) {
            belowPipe.acceptFluid(slug, this.storedAmount, entryFace);
            return true;
        }
        
        if (this.mode != PipeMode.INSERT || forwardBe == null) return false;

        IFluidHandler handler = forwardBe
                .getCapability(ForgeCapabilities.FLUID_HANDLER, entryFace)
                .orElse(null);
        if (handler == null) return false;

        int inserted = handler.fill(slug, IFluidHandler.FluidAction.EXECUTE);
        if (inserted <= 0) return false;

        this.storedAmount -= inserted;
        if (this.storedAmount <= 0) {
            resetFluid();
        } else {
            this.storedFluid.setAmount(this.storedAmount);
        }
        return true;
    }
    
    private class PipeFluidHandler implements IFluidHandler {
        private final Direction side;

        private PipeFluidHandler(Direction side) {
            this.side = side;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return isOutputFace(pipeState(), side) ? getStoredStack() : FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return 0;
            if (!isEmptyPipe() || travelDirection != null) return 0;
            if (!isConnectedEnd(pipeState(), side)) return 0;

            int accepted = Math.min(resource.getAmount(), CAPACITY);
            if (action.execute()) {
                storedFluid = resource.copy();
                storedFluid.setAmount(accepted);
                storedAmount = accepted;
                travelDirection = side;
                sync();
            }
            return accepted;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return FluidStack.EMPTY;
            if (!isOutputFace(pipeState(), side)) return FluidStack.EMPTY;

            FluidStack stored = getStoredStack();
            if (stored.isEmpty() || !stored.isFluidEqual(resource)) return FluidStack.EMPTY;
            return drainStored(Math.min(resource.getAmount(), stored.getAmount()), action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (!isOutputFace(pipeState(), side)) return FluidStack.EMPTY;
            return drainStored(maxDrain, action);
        }
    }

    private FluidStack drainStored(int maxDrain, IFluidHandler.FluidAction action) {
        if (isEmptyPipe() || maxDrain <= 0) return FluidStack.EMPTY;

        int drained = Math.min(maxDrain, this.storedAmount);
        FluidStack out = this.storedFluid.copy();
        out.setAmount(drained);

        if (action.execute()) {
            this.storedAmount -= drained;
            if (this.storedAmount <= 0) {
                resetFluid();
            } else {
                this.storedFluid.setAmount(this.storedAmount);
            }
            sync();
        }
        return out;
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("storedAmount", storedAmount);
        if (!this.storedFluid.isEmpty()) {
            tag.put("storedFluid", this.storedFluid.writeToNBT(new CompoundTag()));
        }
        if (this.travelDirection != null) {
            tag.putInt("travelDirection", this.travelDirection.get3DDataValue());
        }
        tag.putInt("mode", this.mode.ordinal());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.storedAmount = tag.getInt("storedAmount");
        if (tag.contains("storedFluid")) {
            this.storedFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("storedFluid"));
        } else {
            this.storedFluid = FluidStack.EMPTY;
        }
        if (this.storedFluid.isEmpty()) {
            this.storedAmount = 0;
        }
        if (tag.contains("travelDirection")) {
            this.travelDirection = Direction.from3DDataValue(tag.getInt("travelDirection"));
        } else {
            this.travelDirection = null;
        }
        if (tag.contains("mode")) {
            int id = tag.getInt("mode");
            this.mode = (id >= 0 && id < PipeMode.values().length) ? PipeMode.values()[id] : PipeMode.TRANSFER;
        } else {
            this.mode = PipeMode.TRANSFER;
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    public enum PipeMode {
        TRANSFER,
        EXTRACT,
        INSERT;

        public PipeMode next() {
            return switch (this) {
                case TRANSFER -> EXTRACT;
                case EXTRACT -> INSERT;
                case INSERT -> TRANSFER;
            };
        }
    }
}
