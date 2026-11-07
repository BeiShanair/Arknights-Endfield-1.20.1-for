package com.besson.endfield.blockEntity.custom.production1;

import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.RefiningUnitBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.BaseIOBlockEntity;
import com.besson.endfield.recipe.InputEntry;
import com.besson.endfield.recipe.custom.RefiningUnitRecipe;
import com.besson.endfield.screen.custom.production1.RefiningUnitScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

import static com.besson.endfield.block.ModBlockEntityWithFacing.FACING;

public class RefiningUnitBlockEntity extends BaseIOBlockEntity<RefiningUnitRecipe> implements GeoBlockEntity {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private static final int POWER_PRE_TICK = 5;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final int FLUID_CAPACITY = 50000;
    public static final int SEWAGE_CAPACITY = 50000;
    
    public boolean nowFluidMode = false;
    public final FluidTank fluidTank = new FluidTank(FLUID_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };
    
    public final FluidTank sewageTank = new FluidTank(SEWAGE_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };
    
    private final IFluidHandler sidedFluidInterface = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 2;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return tank == 0 ? fluidTank.getFluid() : sewageTank.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return tank == 0 ? fluidTank.getCapacity() : sewageTank.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return tank == 0;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!nowFluidMode) return 0;
            return fluidTank.fill(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            return sewageTank.drain(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return sewageTank.drain(maxDrain, action);
        }
    };

    private LazyOptional<IFluidHandler> fluidInterfaceLazy = LazyOptional.empty();

    public RefiningUnitBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFINING_UNIT.get(), pos, state, 40);
    }
    
    public void changeFluidMode(Level world) {
        nowFluidMode = !nowFluidMode;
        Direction facing = getBlockState().getValue(RefiningUnitBlock.FACING);
        BlockPos left = worldPosition.relative(facing.getCounterClockWise()).above(2);
        BlockPos right = worldPosition.relative(facing.getClockWise()).above(2);

        if (nowFluidMode) {
            world.setBlockAndUpdate(left, ModBlocks.REFINING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, facing));
            world.setBlockAndUpdate(right, ModBlocks.REFINING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, facing));
            if (world.getBlockEntity(left) instanceof RefiningUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(worldPosition);
            }
            if (world.getBlockEntity(right) instanceof RefiningUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(worldPosition);
            }
        } else {
            clearFluidTanks();
            if (world.getBlockEntity(left) instanceof RefiningUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(null);
            }
            if (world.getBlockEntity(right) instanceof RefiningUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(null);
            }
            world.removeBlock(left, false);
            world.removeBlock(right, false);
        }
        setChanged();
        world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
    
    public void clearFluidTanks() {
        boolean changed = !fluidTank.isEmpty() || !sewageTank.isEmpty();
        fluidTank.setFluid(FluidStack.EMPTY);
        sewageTank.setFluid(FluidStack.EMPTY);
        if (changed) {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }
    
    /**
     * 流体模式下，主机任意面都可以作为流体端口。
     * 注意：主机四周 8 个水平面全部被 REFINING_UNIT_SIDE 占住，若只放行左右两面，
     * 外部管道实际上永远碰不到主机的水平面，只剩顶/底两个面可用，表现为“接管到底座注不进去”。
     */
    @Nullable
    public IFluidHandler getFluidStorage(Direction side) {
        if (!nowFluidMode) return null;
        return sidedFluidInterface;
    }
    
    public LazyOptional<IFluidHandler> getFluidInterface() {
        if (!fluidInterfaceLazy.isPresent()) {
            fluidInterfaceLazy = LazyOptional.of(() -> sidedFluidInterface);
        }
        return fluidInterfaceLazy.cast();
    }

    @Override
    protected int getOutputSlotIndex() {
        return OUTPUT_SLOT;
    }

    @Override
    protected int getPowerCostPerTick() {
        return POWER_PRE_TICK;
    }

    @Override
    protected ContainerData createPropertyDelegate() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> RefiningUnitBlockEntity.this.progress;
                    case 1 -> RefiningUnitBlockEntity.this.maxProgress;
                    case 2 -> RefiningUnitBlockEntity.this.enable ? 1 : 0;
                    case 3 -> fluidTank.getFluidAmount();
                    case 4 -> fluidTank.getCapacity();
                    case 5 -> sewageTank.getFluidAmount();
                    case 6 -> sewageTank.getCapacity();
                    case 7 -> RefiningUnitBlockEntity.this.storedPower;
                    case 8 -> RefiningUnitBlockEntity.this.nowFluidMode ? 1 : 0;
                    case 9 -> fluidId(fluidTank.getFluid().getFluid());
                    case 10 -> fluidId(sewageTank.getFluid().getFluid());
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RefiningUnitBlockEntity.this.progress = value;
                    case 1 -> RefiningUnitBlockEntity.this.maxProgress = value;
                    case 2 -> RefiningUnitBlockEntity.this.enable = value == 1;
                }
            }

            @Override
            public int getCount() {
                return 11;
            }
        };
    }

    private static int fluidId(@Nullable Fluid fluid) {
        return BuiltInRegistries.FLUID.getId(fluid == null ? Fluids.WATER : fluid);
    }

    @Override
    protected int getInvSize() {
        return 2;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 4, 0);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return LazyOptional.of(() -> itemStackHandler).cast();
            }
            Direction facing = getBlockState().getValue(RefiningUnitBlock.FACING);
            if (side == facing) {
                if (!input.isPresent()) {
                    input = LazyOptional.of(() -> new InputItemHandler(itemStackHandler));
                }
                return input.cast();
            } else if (side == facing.getOpposite()) {
                if (!output.isPresent()) {
                    output = LazyOptional.of(() -> new OutputItemHandler(itemStackHandler));
                }
                return output.cast();
            }
        }
        
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (getFluidStorage(side) != null) {
                return getFluidInterface().cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidInterfaceLazy.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        input = LazyOptional.of(() -> new InputItemHandler(itemStackHandler));
        output = LazyOptional.of(() -> new OutputItemHandler(itemStackHandler));
        fluidInterfaceLazy = LazyOptional.of(() -> sidedFluidInterface);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.refining_unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new RefiningUnitScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void craftItem(Level world) {
        getMatchRecipe(world).ifPresent(r -> {
            ItemStack result = r.getResultItem(world.registryAccess());
            itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
            InputEntry recipeInput = r.getInput();

            ItemStack stack = itemStackHandler.getStackInSlot(0);
            if (recipeInput.ingredient().test(stack) && stack.getCount() >= recipeInput.count()) {
                itemStackHandler.extractItem(0, recipeInput.count(), false);
            }

            if (r.requiresFluid()) {
                int required = r.getRequiredFluidAmount();
                ResourceLocation reqLoc = r.getRequiredFluid();
                if (reqLoc != null) {
                    var reqFluid = ForgeRegistries.FLUIDS.getValue(reqLoc);
                    if (reqFluid != null) {
                        FluidStack simulated = fluidTank.drain(required, IFluidHandler.FluidAction.SIMULATE);
                        if (!simulated.isEmpty() && simulated.getFluid().isSame(reqFluid) && simulated.getAmount() >= required) {
                            fluidTank.drain(required, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }

            if (r.producesFluid()) {
                int amount = r.getOutputFluidAmount();
                ResourceLocation outLoc = r.getOutputFluid();
                if (outLoc != null) {
                    var outFluid = ForgeRegistries.FLUIDS.getValue(outLoc);
                    if (outFluid != null) {
                        sewageTank.fill(new FluidStack(outFluid, amount), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        });
    }

    @Override
    protected Optional<RefiningUnitRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(2);
        for (int i = 0; i < 2; i++) {
            inv.setItem(i, this.itemStackHandler.getStackInSlot(i));
        }
        return world.getRecipeManager()
                .getRecipeFor(RefiningUnitRecipe.Type.INSTANCE, inv, world);
    }

    @Override
    protected boolean hasCorrectRecipe(Level world) {
        Optional<RefiningUnitRecipe> match = getMatchRecipe(world);
        if (match.isPresent()) {
            RefiningUnitRecipe recipe = match.get();
            InputEntry recipeInput = recipe.getInput();
            boolean matched = false;
            ItemStack stack = this.itemStackHandler.getStackInSlot(0);
            if (recipeInput.ingredient().test(stack) && stack.getCount() >= recipeInput.count()) {
                matched = true;
            }
            if (!matched) return false;

            if (recipe.requiresFluid()) {
                ResourceLocation reqLoc = recipe.getRequiredFluid();
                if (reqLoc == null) return false;
                var reqFluid = ForgeRegistries.FLUIDS.getValue(reqLoc);
                if (reqFluid == null) return false;
                FluidStack current = fluidTank.getFluid();
                if (current.isEmpty() || !current.getFluid().isSame(reqFluid) || current.getAmount() < recipe.getRequiredFluidAmount()) {
                    return false;
                }
            }

            if (recipe.producesFluid()) {
                int amount = recipe.getOutputFluidAmount();
                int spaceLeft = SEWAGE_CAPACITY - sewageTank.getFluidAmount();
                if (spaceLeft < amount) return false;
                FluidStack currentS = sewageTank.getFluid();
                if (!currentS.isEmpty()) {
                    ResourceLocation outLoc = recipe.getOutputFluid();
                    if (outLoc == null) return false;
                    var outFluid = ForgeRegistries.FLUIDS.getValue(outLoc);
                    if (outFluid == null) return false;
                    if (!currentS.getFluid().isSame(outFluid)) return false;
                }
            }

            ItemStack result = recipe.getResultItem(world.registryAccess());
            return canOutputAccept(result);
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("FluidTank", fluidTank.writeToNBT(new CompoundTag()));
        tag.put("SewageTank", sewageTank.writeToNBT(new CompoundTag()));
        tag.putBoolean("fluidMode", nowFluidMode);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("FluidTank")) {
            fluidTank.readFromNBT(tag.getCompound("FluidTank"));
        }
        if (tag.contains("SewageTank")) {
            sewageTank.readFromNBT(tag.getCompound("SewageTank"));
        }
        nowFluidMode = tag.getBoolean("fluidMode");
    }
    
    public Component getFluidMode() {
        return Component.translatable(nowFluidMode ? "endfield.fluid_mode.open" : "endfield.fluid_mode.close");
    }

    private record InputItemHandler(ItemStackHandler parent) implements IItemHandler {

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return parent.getStackInSlot(RefiningUnitBlockEntity.INPUT_SLOT);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack current = parent.getStackInSlot(RefiningUnitBlockEntity.INPUT_SLOT);
            if (current.isEmpty() || ItemStack.isSameItemSameTags(current, stack)) {
                return parent.insertItem(RefiningUnitBlockEntity.INPUT_SLOT, stack, simulate);
            }
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return true;
        }
    }

    private record OutputItemHandler(ItemStackHandler parent) implements IItemHandler {

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return parent.getStackInSlot(RefiningUnitBlockEntity.OUTPUT_SLOT);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return parent.extractItem(RefiningUnitBlockEntity.OUTPUT_SLOT, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }
    }
}
