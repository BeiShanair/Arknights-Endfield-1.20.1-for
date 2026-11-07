package com.besson.endfield.blockEntity.custom.production2;

import com.besson.endfield.blockEntity.custom.logicitis.BeltBlockEntity;
import com.besson.endfield.blockEntity.custom.logicitis.FluidPipeBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BasePylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BaseRelayBlockEntity;
import com.besson.endfield.recipe.ReactorResource;
import com.besson.endfield.recipe.custom.ReactorCrucibleRecipe;
import com.besson.endfield.util.power.NodeEntry;
import com.besson.endfield.util.power.NodeType;
import com.besson.endfield.util.power.PowerNetworkManager;
import com.besson.endfield.util.power.PowerNetworkNodeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseCrucibleBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {
    public static final long MAX_ITEM_AMOUNT = 64;
    public static final long MAX_FLUID_AMOUNT = 50000;
    protected static final int MAX_PROGRESS = 40;
    protected static final int MAX_STORED_POWER = 10000;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final int resourceSlotCount;
    protected final int powerCostPerTick;

    protected final Map<ReactorResource, Long> resources = new LinkedHashMap<>();
    @Nullable
    protected ReactorResource itemOutputFilter;
    @Nullable
    protected final ReactorResource[] fluidOutputFilters = new ReactorResource[2];
    protected int progress;

    protected int tickNum = 0;
    protected boolean isPowered = false;
    protected int storedPower = 0;
    protected boolean needsInit = true;

    private final LazyOptional<IItemHandler> itemHandlerLazy = LazyOptional.of(SharedItemHandler::new);
    private final IFluidHandler inputFluidHandler = new InputFluidHandler();
    private final LazyOptional<IFluidHandler> inputFluidLazy = LazyOptional.of(() -> inputFluidHandler);
    private final LazyOptional<IFluidHandler>[] portFluidLazy;
    
    @SuppressWarnings("unchecked")
    protected BaseCrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                      int resourceSlotCount, int powerCostPerTick) {
        super(type, pos, state);
        this.resourceSlotCount = resourceSlotCount;
        this.powerCostPerTick = powerCostPerTick;
        this.portFluidLazy = new LazyOptional[]{LazyOptional.empty(), LazyOptional.empty(), LazyOptional.empty()};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BaseCrucibleBlockEntity be) {
        if (level.isClientSide()) return;

        if (be.needsInit && level instanceof ServerLevel serverLevel) {
            be.needsInit = false;
            PowerNetworkManager.get(serverLevel).registerConsumer(be.getBlockPos(), be::getRequiredPower, be::receiveElectricCharge);
        }

        be.tickNum++;
        if (be.tickNum % 20 == 0 && level instanceof ServerLevel serverLevel) {
            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverLevel);
            boolean powered = false;

            Optional<NodeEntry> nearest = manager.findNearest(pos, NodeType.CONSUMER, 10);
            if (nearest.isPresent()) {
                BlockEntity b = level.getBlockEntity(nearest.get().pos());
                powered = b instanceof BasePylonBlockEntity || b instanceof BaseRelayBlockEntity;
            }

            be.isPowered = powered;
            be.tickNum = 0;
        }
        
        if (!be.isPowered && be.storedPower < be.powerCostPerTick) {
            if (be.progress != 0) {
                be.progress = 0;
                be.sync();
            }
            return;
        }

        List<ReactorCrucibleRecipe> plan = be.createCraftPlan(level);
        if (plan.isEmpty()) {
            be.progress = 0;
            be.sync();
            return;
        }

        be.progress++;
        be.storedPower -= be.powerCostPerTick;
        if (be.progress >= MAX_PROGRESS) {
            be.applyCraftPlan(plan);
            be.progress = 0;
        }
        be.sync();
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level instanceof ServerLevel) {
            needsInit = true;
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            PowerNetworkManager.get(serverLevel).unregisterConsumer(this.worldPosition);
        }
        super.setRemoved();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerLazy.invalidate();
        inputFluidLazy.invalidate();
        for (LazyOptional<IFluidHandler> lazy : portFluidLazy) {
            lazy.invalidate();
        }
    }

    public void receiveElectricCharge(int amount) {
        this.storedPower = Math.min(this.storedPower + amount * 20, MAX_STORED_POWER);
    }

    public int getRequiredPower() {
        if (isPowered && storedPower < MAX_STORED_POWER) {
            return powerCostPerTick;
        }
        return 0;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (level != null && side != null
                    && level.getBlockEntity(worldPosition.relative(side)) instanceof BeltBlockEntity) {
                return itemHandlerLazy.cast();
            }
            return super.getCapability(cap, side);
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (level != null && side != null
                    && level.getBlockEntity(worldPosition.relative(side)) instanceof FluidPipeBlockEntity) {
                return inputFluidLazy.cast();
            }
            return super.getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }
    public LazyOptional<IItemHandler> getSharedItemHandler() {
        return itemHandlerLazy;
    }
    
    public LazyOptional<IFluidHandler> getPortFluidHandler(int portIndex) {
        int index = portIndex < 0 ? 0 : Math.min(portIndex, 1) + 1;
        if (!portFluidLazy[index].isPresent()) {
            portFluidLazy[index] = LazyOptional.of(() -> new PortFluidHandler(portIndex));
        }
        return portFluidLazy[index];
    }

    public List<StoredResource> getDisplayResources() {
        List<StoredResource> displayResources = new ArrayList<>();
        for (Map.Entry<ReactorResource, Long> entry : resources.entrySet()) {
            if (!entry.getKey().isBlank() && entry.getValue() > 0) {
                displayResources.add(new StoredResource(entry.getKey(), entry.getValue()));
            }
        }
        return displayResources;
    }

    public void setOutputFromDisplaySlot(int slot) {
        List<StoredResource> displayResources = getDisplayResources();
        if (slot < 0 || slot >= displayResources.size()) return;

        ReactorResource resource = displayResources.get(slot).resource();
        if (resource.isItem()) {
            this.itemOutputFilter = resource;
        } else if (resource.isFluid()) {
            int target = fluidOutputFilters[0] == null || fluidOutputFilters[0].equals(resource) ? 0 : 1;
            this.fluidOutputFilters[target] = resource;
        }
        sync();
    }
    
    public void clearOutputFilter(int index) {
        if (index == 0) {
            if (itemOutputFilter == null) return;
            itemOutputFilter = null;
        } else if (index >= 1 && index <= 2) {
            int target = index - 1;
            if (fluidOutputFilters[target] == null) return;
            fluidOutputFilters[target] = null;
        } else {
            return;
        }
        sync();
    }

    public ItemStack getItemOutputFilter() {
        return itemOutputFilter != null && itemOutputFilter.isItem()
                ? itemOutputFilter.asStack(1)
                : ItemStack.EMPTY;
    }

    public FluidStack getFluidOutputFilter(int index) {
        ReactorResource resource = fluidOutputFilters[index];
        return resource != null && resource.isFluid() ? resource.asFluidStack(1) : FluidStack.EMPTY;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return MAX_PROGRESS;
    }

    public int getResourceSlotCount() {
        return resourceSlotCount;
    }

    public int getPowerCostPerTick() {
        return powerCostPerTick;
    }

    private List<ReactorCrucibleRecipe> createCraftPlan(Level level) {
        List<ReactorCrucibleRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(ReactorCrucibleRecipe.Type.INSTANCE);
        Map<ReactorResource, Long> availableInputs = new LinkedHashMap<>(resources);
        Map<ReactorResource, Long> plannedOutputs = new LinkedHashMap<>();
        List<ReactorCrucibleRecipe> plan = new ArrayList<>();

        for (ReactorCrucibleRecipe recipe : recipes) {
            if (!recipe.canCraft(availableInputs)) continue;

            Map<ReactorResource, Long> candidateInputs = new LinkedHashMap<>(availableInputs);
            recipe.consumeInputs(candidateInputs);

            Map<ReactorResource, Long> candidateFinalState = new LinkedHashMap<>(candidateInputs);
            mergeResources(candidateFinalState, plannedOutputs);
            if (!recipe.canFitOutputs(candidateFinalState, resourceSlotCount)) continue;

            availableInputs = candidateInputs;
            recipe.addOutputs(plannedOutputs);
            plan.add(recipe);
        }
        return plan;
    }

    private void applyCraftPlan(List<ReactorCrucibleRecipe> plan) {
        Map<ReactorResource, Long> finalResources = new LinkedHashMap<>(resources);
        for (ReactorCrucibleRecipe recipe : plan) {
            if (!recipe.canCraft(finalResources)) return;
            recipe.consumeInputs(finalResources);
        }
        for (ReactorCrucibleRecipe recipe : plan) {
            if (!recipe.canFitOutputs(finalResources, resourceSlotCount)) return;
            recipe.addOutputs(finalResources);
        }
        resources.clear();
        resources.putAll(finalResources);
        cleanResources();
    }

    private void mergeResources(Map<ReactorResource, Long> target, Map<ReactorResource, Long> source) {
        for (Map.Entry<ReactorResource, Long> entry : source.entrySet()) {
            target.merge(entry.getKey(), entry.getValue(), Long::sum);
        }
    }

    private long getInsertableAmount(ReactorResource resource, long maxAmount) {
        if (resource.isBlank() || maxAmount <= 0) return 0;
        long stored = resources.getOrDefault(resource, 0L);
        if (stored <= 0 && resources.size() >= resourceSlotCount) return 0;
        return Math.min(maxAmount, resource.getMaxAmount() - stored);
    }

    private void insertResource(ReactorResource resource, long amount) {
        if (resource.isBlank() || amount <= 0) return;
        resources.merge(resource, amount, Long::sum);
        cleanResources();
    }

    private long extractResource(ReactorResource resource, long maxAmount) {
        long stored = resources.getOrDefault(resource, 0L);
        long extracted = Math.min(stored, maxAmount);
        if (extracted > 0) {
            resources.put(resource, stored - extracted);
            cleanResources();
        }
        return extracted;
    }

    private void cleanResources() {
        resources.entrySet().removeIf(entry -> entry.getKey().isBlank() || entry.getValue() <= 0);
    }

    protected void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public abstract Component getDisplayName();

    @Override
    public abstract @Nullable AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag resourceList = new ListTag();
        for (Map.Entry<ReactorResource, Long> entry : resources.entrySet()) {
            CompoundTag compound = new CompoundTag();
            compound.put("resource", entry.getKey().toNbt());
            compound.putLong("amount", entry.getValue());
            resourceList.add(compound);
        }
        tag.put("resources", resourceList);

        if (itemOutputFilter != null) tag.put("itemOutputFilter", itemOutputFilter.toNbt());
        ListTag fluidFilters = new ListTag();
        for (ReactorResource filter : fluidOutputFilters) {
            if (filter != null) fluidFilters.add(filter.toNbt());
        }
        tag.put("fluidOutputFilters", fluidFilters);
        tag.putInt("progress", progress);
        tag.putInt("storedPower", storedPower);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        resources.clear();

        ListTag resourceList = tag.getList("resources", Tag.TAG_COMPOUND);
        for (int i = 0; i < resourceList.size(); i++) {
            CompoundTag compound = resourceList.getCompound(i);
            ReactorResource resource = ReactorResource.fromNbt(compound.getCompound("resource"));
            long amount = Math.min(compound.getLong("amount"), resource.getMaxAmount());
            if (!resource.isBlank() && amount > 0 && resources.size() < resourceSlotCount) {
                resources.put(resource, amount);
            }
        }

        itemOutputFilter = tag.contains("itemOutputFilter")
                ? ReactorResource.fromNbt(tag.getCompound("itemOutputFilter"))
                : null;
        ListTag fluidFilters = tag.getList("fluidOutputFilters", Tag.TAG_COMPOUND);
        for (int i = 0; i < fluidOutputFilters.length; i++) {
            fluidOutputFilters[i] = i < fluidFilters.size()
                    ? ReactorResource.fromNbt(fluidFilters.getCompound(i))
                    : null;
        }
        progress = tag.getInt("progress");
        storedPower = tag.getInt("storedPower");
        cleanResources();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    private class SharedItemHandler implements IItemHandler {
        @Override
        public int getSlots() {
            return resourceSlotCount;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot != 0 || itemOutputFilter == null) return ItemStack.EMPTY;
            long amount = resources.getOrDefault(itemOutputFilter, 0L);
            return amount <= 0 ? ItemStack.EMPTY : itemOutputFilter.asStack((int) Math.min(amount, MAX_ITEM_AMOUNT));
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return ItemStack.EMPTY;
            long accepted = getInsertableAmount(ReactorResource.item(stack), stack.getCount());
            if (accepted <= 0) return stack;

            ItemStack remainder = stack.copy();
            remainder.shrink((int) accepted);
            if (!simulate) {
                insertResource(ReactorResource.item(stack), accepted);
                sync();
            }
            return remainder;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != 0 || itemOutputFilter == null || amount <= 0) return ItemStack.EMPTY;
            long stored = resources.getOrDefault(itemOutputFilter, 0L);
            int extracted = (int) Math.min(Math.min(stored, amount), MAX_ITEM_AMOUNT);
            if (extracted <= 0) return ItemStack.EMPTY;
            if (!simulate) {
                extractResource(itemOutputFilter, extracted);
                sync();
            }
            return itemOutputFilter.asStack(extracted);
        }

        @Override
        public int getSlotLimit(int slot) {
            return (int) MAX_ITEM_AMOUNT;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }
    }
    
    private class InputFluidHandler implements IFluidHandler {
        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return (int) MAX_FLUID_AMOUNT;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return !stack.isEmpty();
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return 0;
            long inserted = getInsertableAmount(ReactorResource.fluid(resource), resource.getAmount());
            if (inserted <= 0) return 0;
            if (action.execute()) {
                insertResource(ReactorResource.fluid(resource), inserted);
                sync();
            }
            return (int) inserted;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }
    }
    
    private class PortFluidHandler extends InputFluidHandler {
        private final int portIndex;

        private PortFluidHandler(int portIndex) {
            this.portIndex = portIndex;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            if (portIndex < 0) return FluidStack.EMPTY;
            ReactorResource filter = fluidOutputFilters[portIndex];
            if (filter == null || filter.isBlank()) return FluidStack.EMPTY;
            long stored = resources.getOrDefault(filter, 0L);
            return stored <= 0 ? FluidStack.EMPTY : filter.asFluidStack((int) Math.min(stored, MAX_FLUID_AMOUNT));
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (portIndex < 0 || resource.isEmpty()) return FluidStack.EMPTY;
            ReactorResource filter = fluidOutputFilters[portIndex];
            if (filter == null || !filter.equals(ReactorResource.fluid(resource))) return FluidStack.EMPTY;
            return drain(resource.getAmount(), action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (portIndex < 0 || maxDrain <= 0) return FluidStack.EMPTY;
            ReactorResource filter = fluidOutputFilters[portIndex];
            if (filter == null || filter.isBlank()) return FluidStack.EMPTY;
            long stored = resources.getOrDefault(filter, 0L);
            int drained = (int) Math.min(Math.min(stored, maxDrain), MAX_FLUID_AMOUNT);
            if (drained <= 0) return FluidStack.EMPTY;
            if (action.execute()) {
                extractResource(filter, drained);
                sync();
            }
            return filter.asFluidStack(drained);
        }
    }

    public record StoredResource(ReactorResource resource, long amount) {
        public boolean item() {
            return resource.isItem();
        }

        public boolean fluid() {
            return resource.isFluid();
        }

        public ItemStack itemStack() {
            return resource.itemStack();
        }

        public FluidStack fluidStack() {
            return resource.asFluidStack(1);
        }

        public ItemStack asStack() {
            return resource.asStack((int) Math.min(amount, MAX_ITEM_AMOUNT));
        }
    }
}
