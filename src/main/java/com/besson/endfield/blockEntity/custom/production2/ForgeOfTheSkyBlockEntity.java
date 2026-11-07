package com.besson.endfield.blockEntity.custom.production2;

import com.besson.endfield.block.custom.production2.ForgeOfTheSkyBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.BaseIOBlockEntity;
import com.besson.endfield.recipe.InputEntry;
import com.besson.endfield.recipe.custom.ForgeOfTheSkyRecipe;
import com.besson.endfield.screen.custom.production2.ForgeOfTheSkyScreenHandler;
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
import net.minecraft.world.level.block.state.BlockState;
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

public class ForgeOfTheSkyBlockEntity extends BaseIOBlockEntity<ForgeOfTheSkyRecipe> implements GeoBlockEntity {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private static final int POWER_PRE_TICK = 50;
    
    public static final int DEFAULT_CRAFT_TIME = 40;
    public static final int FLUID_CAPACITY = 50000;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    public final FluidTank fluidTank = new FluidTank(FLUID_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };
    
    private final LazyOptional<IFluidHandler> insertOnlyFluidLazy = LazyOptional.of(() -> new IFluidHandler() {
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
    });

    public ForgeOfTheSkyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FORGE_OF_THE_SKY.get(), pos, state, DEFAULT_CRAFT_TIME);
    }

    @Override
    protected int getInvSize() {
        return 2;
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
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 4, 0);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return LazyOptional.of(() -> itemStackHandler).cast();
            }
            Direction facing = getBlockState().getValue(ForgeOfTheSkyBlock.FACING);
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
            return insertOnlyFluidLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        input = LazyOptional.of(() -> new InputItemHandler(itemStackHandler));
        output = LazyOptional.of(() -> new OutputItemHandler(itemStackHandler));
    }

    @Override
    protected ContainerData createPropertyDelegate() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ForgeOfTheSkyBlockEntity.this.progress;
                    case 1 -> ForgeOfTheSkyBlockEntity.this.maxProgress;
                    case 2 -> ForgeOfTheSkyBlockEntity.this.enable ? 1 : 0;
                    case 3 -> ForgeOfTheSkyBlockEntity.this.storedPower;
                    case 4 -> fluidTank.getFluidAmount();
                    case 5 -> fluidTank.getCapacity();
                    case 6 -> fluidTank.getFluid().isEmpty()
                            ? BuiltInRegistries.FLUID.getId(Fluids.WATER)
                            : BuiltInRegistries.FLUID.getId(fluidTank.getFluid().getFluid());
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ForgeOfTheSkyBlockEntity.this.progress = value;
                    case 1 -> ForgeOfTheSkyBlockEntity.this.maxProgress = value;
                    case 2 -> ForgeOfTheSkyBlockEntity.this.enable = value == 1;
                }
            }

            @Override
            public int getCount() {
                return 7;
            }
        };
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
        return Component.translatable("blockEntity.forge_of_the_sky");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        return new ForgeOfTheSkyScreenHandler(syncId, inventory, this, this.propertyDelegate);
    }

    @Override
    protected Optional<ForgeOfTheSkyRecipe> getMatchRecipe(Level level) {
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, itemStackHandler.getStackInSlot(INPUT_SLOT));
        Optional<ForgeOfTheSkyRecipe> match = level.getRecipeManager()
                .getRecipeFor(ForgeOfTheSkyRecipe.Type.INSTANCE, inv, level);
        
        int newMax = match.flatMap(ForgeOfTheSkyRecipe::getCraftTime).orElse(DEFAULT_CRAFT_TIME);
        if (newMax != this.maxProgress) {
            this.maxProgress = newMax;
            this.progress = 0;
        }
        return match;
    }

    @Override
    protected void craftItem(Level level) {
        getMatchRecipe(level).ifPresent(r -> {
            ItemStack result = r.getResultItem(level.registryAccess());
            itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));

            InputEntry recipeInput = r.getInput();
            ItemStack stack = itemStackHandler.getStackInSlot(INPUT_SLOT);
            if (recipeInput.ingredient().test(stack) && stack.getCount() >= recipeInput.count()) {
                itemStackHandler.extractItem(INPUT_SLOT, recipeInput.count(), false);
            }

            if (r.requiresFluid()) {
                int required = r.getRequiredFluidAmount();
                ResourceLocation reqLoc = r.getRequiredFluid();
                if (reqLoc != null) {
                    var reqFluid = ForgeRegistries.FLUIDS.getValue(reqLoc);
                    if (reqFluid != null) {
                        FluidStack simulated = fluidTank.drain(required, IFluidHandler.FluidAction.SIMULATE);
                        if (!simulated.isEmpty() && simulated.getFluid().isSame(reqFluid)
                                && simulated.getAmount() >= required) {
                            fluidTank.drain(required, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected boolean hasCorrectRecipe(Level level) {
        Optional<ForgeOfTheSkyRecipe> match = getMatchRecipe(level);
        if (match.isEmpty()) return false;

        ForgeOfTheSkyRecipe recipe = match.get();

        InputEntry recipeInput = recipe.getInput();
        ItemStack stack = itemStackHandler.getStackInSlot(INPUT_SLOT);
        if (!recipeInput.ingredient().test(stack) || stack.getCount() < recipeInput.count()) {
            return false;
        }

        if (recipe.requiresFluid()) {
            ResourceLocation reqLoc = recipe.getRequiredFluid();
            if (reqLoc == null) return false;
            var reqFluid = ForgeRegistries.FLUIDS.getValue(reqLoc);
            if (reqFluid == null) return false;

            FluidStack current = fluidTank.getFluid();
            if (current.isEmpty() || !current.getFluid().isSame(reqFluid)
                    || current.getAmount() < recipe.getRequiredFluidAmount()) {
                return false;
            }
        }

        return canOutputAccept(recipe.getResultItem(level.registryAccess()));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("FluidTank", fluidTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("FluidTank")) {
            fluidTank.readFromNBT(tag.getCompound("FluidTank"));
        }
    }

    private record InputItemHandler(ItemStackHandler parent) implements IItemHandler {
        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return parent.getStackInSlot(INPUT_SLOT);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            ItemStack current = parent.getStackInSlot(INPUT_SLOT);
            if (current.isEmpty() || ItemStack.isSameItemSameTags(current, stack)) {
                return parent.insertItem(INPUT_SLOT, stack, simulate);
            }
            return stack;
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
