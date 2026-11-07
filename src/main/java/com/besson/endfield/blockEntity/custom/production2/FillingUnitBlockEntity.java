package com.besson.endfield.blockEntity.custom.production2;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production2.FillingUnitBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.BaseIOBlockEntity;
import com.besson.endfield.recipe.InputEntry;
import com.besson.endfield.recipe.custom.FillingUnitRecipe;
import com.besson.endfield.screen.custom.production2.FillingUnitScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
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
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.besson.endfield.block.ModBlockEntityWithFacing.FACING;

public class FillingUnitBlockEntity extends BaseIOBlockEntity<FillingUnitRecipe> implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final int INPUT_SLOT1 = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int POWER_PRE_TICK = 20;
    public static final int FLUID_CAPACITY = 50000;
    
    public static final int CONVERTIBLE_INPUT_SLOT = INPUT_SLOT2;
    
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
    
    private final IFluidHandler sidedFluidInterface = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return fluidTank.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return fluidTank.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return fluidTank.isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!nowFluidMode) return 0;
            return fluidTank.fill(resource, action);
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }
    };

    private LazyOptional<IFluidHandler> fluidInterfaceLazy = LazyOptional.empty();

    public FillingUnitBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILLING_UNIT.get(), pos, state, FillingUnitRecipe.DEFAULT_PROCESSING_TIME);
    }
    
    public void changeFluidMode(Level world, Player player) {
        Direction facing = getBlockState().getValue(FillingUnitBlock.FACING);
        BlockPos[] tankPositions = FillingUnitBlock.getFluidTankPositions(facing, worldPosition);
        BlockPos left = tankPositions[0];
        BlockPos right = tankPositions[1];

        if (!nowFluidMode) {
            if (!MultiBlockPlacementUtil.isReplaceable(world, left) || !MultiBlockPlacementUtil.isReplaceable(world, right)) {
                player.displayClientMessage(Component.translatable("endfield.fluid_mode.blocked"), true);
                return;
            }
            returnConvertibleSlotItems(player);
            nowFluidMode = true;

            world.setBlockAndUpdate(left, ModBlocks.FILLING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, facing));
            world.setBlockAndUpdate(right, ModBlocks.FILLING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, facing));
            if (world.getBlockEntity(left) instanceof FillingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(worldPosition);
            }
            if (world.getBlockEntity(right) instanceof FillingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(worldPosition);
            }
        } else {
            returnConvertibleSlotItems(player);
            clearFluidTank();
            nowFluidMode = false;

            if (world.getBlockEntity(left) instanceof FillingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(null);
            }
            if (world.getBlockEntity(right) instanceof FillingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(null);
            }
            world.removeBlock(left, false);
            world.removeBlock(right, false);
        }
        setChanged();
        world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
    
    private void returnConvertibleSlotItems(Player player) {
        ItemStack stack = itemStackHandler.extractItem(CONVERTIBLE_INPUT_SLOT, 64, false);
        if (!stack.isEmpty() && !player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
    
    public void clearFluidTank() {
        boolean changed = !fluidTank.isEmpty();
        fluidTank.setFluid(FluidStack.EMPTY);
        if (changed) {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }
    
    /**
     * 流体模式下，主机任意面都可以作为流体端口。
     * 注意：主机四周的水平面全部被 FILLING_UNIT_SIDE 占住，若只放行左右两面，
     * 外部管道实际上永远碰不到主机的水平面，表现为“接管到底座注不进去”。
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
    protected int getInvSize() {
        return 3;
    }

    @Override
    protected ContainerData createPropertyDelegate() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FillingUnitBlockEntity.this.progress;
                    case 1 -> FillingUnitBlockEntity.this.maxProgress;
                    case 2 -> FillingUnitBlockEntity.this.enable ? 1 : 0;
                    case 3 -> FillingUnitBlockEntity.this.storedPower;
                    case 4 -> fluidTank.getFluidAmount();
                    case 5 -> fluidTank.getCapacity();
                    case 6 -> FillingUnitBlockEntity.this.nowFluidMode ? 1 : 0;
                    case 7 -> fluidId(fluidTank.getFluid().getFluid());
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FillingUnitBlockEntity.this.progress = value;
                    case 1 -> FillingUnitBlockEntity.this.maxProgress = value;
                    case 2 -> FillingUnitBlockEntity.this.enable = value == 1;
                }
            }

            @Override
            public int getCount() {
                return 8;
            }
        };
    }

    private static int fluidId(@Nullable Fluid fluid) {
        return BuiltInRegistries.FLUID.getId(fluid == null ? Fluids.WATER : fluid);
    }

    @Override
    protected int getPowerCostPerTick() {
        return POWER_PRE_TICK;
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
            Direction facing = this.getBlockState().getValue(FillingUnitBlock.FACING);
            if (side == facing.getOpposite()) {
                if (!output.isPresent()) {
                    output = LazyOptional.of(() -> new OutputItemHandler(itemStackHandler));
                }
                return output.cast();
            } else if (side == facing) {
                if (!input.isPresent()) {
                    input = LazyOptional.of(() -> new InputItemHandler(this));
                }
                return input.cast();
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
        input = LazyOptional.of(() -> new InputItemHandler(this));
        output = LazyOptional.of(() -> new OutputItemHandler(itemStackHandler));
        fluidInterfaceLazy = LazyOptional.of(() -> sidedFluidInterface);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0,
                state -> state.setAndContinue(RawAnimation.begin().thenLoop("working"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.filling_unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FillingUnitScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }

    @Override
    protected Optional<FillingUnitRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(3);
        for (int i = 0; i < 3; i++) {
            inv.setItem(i, this.itemStackHandler.getStackInSlot(i));
        }
        
        List<FillingUnitRecipe> matches = new ArrayList<>();
        for (Recipe<Container> candidate : world.getRecipeManager()
                .getRecipesFor(FillingUnitRecipe.Type.INSTANCE, inv, world)) {
            if (candidate instanceof FillingUnitRecipe recipe) {
                matches.add(recipe);
            }
        }
        for (FillingUnitRecipe recipe : matches) {
            if (nowFluidMode != recipe.requiresFluid()) continue;
            if (nowFluidMode && !hasEnoughFluid(recipe.getRequiredFluid(), recipe.getRequiredFluidAmount())) {
                continue;
            }
            return Optional.of(recipe);
        }
        return Optional.empty();
    }

    private boolean hasEnoughFluid(@Nullable ResourceLocation required, int amount) {
        if (required == null || amount <= 0) return false;
        var fluid = ForgeRegistries.FLUIDS.getValue(required);
        if (fluid == null) return false;
        FluidStack current = fluidTank.getFluid();
        return !current.isEmpty() && current.getFluid().isSame(fluid) && current.getAmount() >= amount;
    }

    @Override
    protected void craftItem(Level world) {
        getMatchRecipe(world).ifPresent(r -> {
            ItemStack result = r.getResultItem(world.registryAccess());
            itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));

            NonNullList<InputEntry> recipeInputs = r.getInput();
            boolean[] used = new boolean[recipeInputs.size()];
            for (InputEntry entry : recipeInputs) {
                for (int i = 0; i < used.length; i++) {
                    ItemStack stack = this.itemStackHandler.getStackInSlot(i);
                    if (!used[i] && entry.ingredient().test(stack)) {
                        itemStackHandler.extractItem(i, entry.count(), false);
                        used[i] = true;
                        break;
                    }
                }
            }
            
            if (r.requiresFluid()) {
                fluidTank.drain(r.getRequiredFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        });
    }

    @Override
    protected boolean hasCorrectRecipe(Level world) {
        Optional<FillingUnitRecipe> match = getMatchRecipe(world);
        if (match.isPresent()) {
            FillingUnitRecipe recipe = match.get();
            this.maxProgress = recipe.getProcessingTime();

            NonNullList<InputEntry> recipeInputs = recipe.getInput();
            boolean[] used = new boolean[recipeInputs.size()];
            for (InputEntry entry : recipeInputs) {
                boolean matched = false;
                for (int i = 0; i < used.length; i++) {
                    ItemStack stack = this.itemStackHandler.getStackInSlot(i);
                    if (!used[i] && entry.ingredient().test(stack) && stack.getCount() >= entry.count()) {
                        matched = true;
                        used[i] = true;
                        break;
                    }
                }
                if (!matched) return false;
            }
            
            if (recipe.requiresFluid()
                    && !hasEnoughFluid(recipe.getRequiredFluid(), recipe.getRequiredFluidAmount())) {
                return false;
            }

            ItemStack result = recipe.getResultItem(world.registryAccess());
            return canOutputAccept(result);
        }
        this.maxProgress = FillingUnitRecipe.DEFAULT_PROCESSING_TIME;
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("FluidTank", fluidTank.writeToNBT(new CompoundTag()));
        tag.putBoolean("fluidMode", nowFluidMode);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("FluidTank")) {
            fluidTank.readFromNBT(tag.getCompound("FluidTank"));
        }
        nowFluidMode = tag.getBoolean("fluidMode");
    }
    
    public Component getFluidMode() {
        return Component.translatable(nowFluidMode ? "endfield.fluid_mode.open" : "endfield.fluid_mode.close");
    }

    private record InputItemHandler(FillingUnitBlockEntity blockEntity,
                                    ItemStackHandler parent) implements IItemHandler {

        private InputItemHandler(FillingUnitBlockEntity blockEntity) {
            this(blockEntity, blockEntity.itemStackHandler);
        }

        @Override
        public int getSlots() {
            return 2;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot == INPUT_SLOT1) return parent.getStackInSlot(INPUT_SLOT1);
            if (slot == INPUT_SLOT2) return parent.getStackInSlot(INPUT_SLOT2);
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return ItemStack.EMPTY;

            if (slot != 0 && slot != 1) return stack;
            
            if (slot == 1 && blockEntity.nowFluidMode) return stack;

            int otherParentIndex = (slot == 0) ? INPUT_SLOT2 : INPUT_SLOT1;
            ItemStack other = parent.getStackInSlot(otherParentIndex);
            if (!other.isEmpty() && other.getItem() == stack.getItem()) {
                return stack;
            }

            int parentIndex = (slot == 0) ? INPUT_SLOT1 : INPUT_SLOT2;
            return parent.insertItem(parentIndex, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }
    }

    private record OutputItemHandler(ItemStackHandler parent) implements IItemHandler {

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return parent.getStackInSlot(OUTPUT_SLOT);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return parent.extractItem(OUTPUT_SLOT, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    }
}
