package com.besson.endfield.blockEntity.custom.production1;

import com.besson.endfield.util.MultiBlockPlacementUtil;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.block.custom.production1.PlantingUnitBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.BaseIOBlockEntity;
import com.besson.endfield.recipe.custom.PlantingUnitRecipe;
import com.besson.endfield.screen.custom.production1.PlantingUnitScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.besson.endfield.block.ModBlockEntityWithFacing.FACING;

public class PlantingUnitBlockEntity extends BaseIOBlockEntity<PlantingUnitRecipe> implements GeoBlockEntity {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private static final int POWER_PRE_TICK = 20;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    public static final int FLUID_CAPACITY = 50000;
    
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

    public PlantingUnitBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLANTING_UNIT.get(), pos, state, 40);
    }
    
    public void changeFluidMode(Level world, Player player) {
        Direction facing = getBlockState().getValue(PlantingUnitBlock.FACING);
        BlockPos[] tankPositions = PlantingUnitBlock.getFluidTankPositions(facing, worldPosition);
        BlockPos left = tankPositions[0];
        BlockPos right = tankPositions[1];

        if (!nowFluidMode) {
            if (!MultiBlockPlacementUtil.isReplaceable(world, left) || !MultiBlockPlacementUtil.isReplaceable(world, right)) {
                player.displayClientMessage(Component.translatable("endfield.fluid_mode.blocked"), true);
                return;
            }
            nowFluidMode = true;

            world.setBlockAndUpdate(left, ModBlocks.PLANTING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, facing));
            world.setBlockAndUpdate(right, ModBlocks.PLANTING_UNIT_SIDE.get().defaultBlockState().setValue(FACING, facing));
            if (world.getBlockEntity(left) instanceof PlantingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(worldPosition);
            }
            if (world.getBlockEntity(right) instanceof PlantingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(worldPosition);
            }
        } else {
            clearFluidTank();
            nowFluidMode = false;

            if (world.getBlockEntity(left) instanceof PlantingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(null);
            }
            if (world.getBlockEntity(right) instanceof PlantingUnitSideBlockEntity adjunct) {
                adjunct.setParentPos(null);
            }
            world.removeBlock(left, false);
            world.removeBlock(right, false);
        }
        setChanged();
        world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
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
     * 注意：主机四周的水平面全部被 PLANTING_UNIT_SIDE 占住，若只放行左右两面，
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
        return 2;
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
                    case 0 -> PlantingUnitBlockEntity.this.progress;
                    case 1 -> PlantingUnitBlockEntity.this.maxProgress;
                    case 2 -> PlantingUnitBlockEntity.this.enable ? 1 : 0;
                    case 3 -> PlantingUnitBlockEntity.this.storedPower;
                    case 4 -> fluidTank.getFluidAmount();
                    case 5 -> fluidTank.getCapacity();
                    case 6 -> PlantingUnitBlockEntity.this.nowFluidMode ? 1 : 0;
                    case 7 -> fluidId(fluidTank.getFluid().getFluid());
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PlantingUnitBlockEntity.this.progress = value;
                    case 1 -> PlantingUnitBlockEntity.this.maxProgress = value;
                    case 2 -> PlantingUnitBlockEntity.this.enable = value == 1;
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
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 4, 0);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return LazyOptional.of(() -> itemStackHandler).cast();
            }
            Direction facing = this.getBlockState().getValue(PlantingUnitBlock.FACING);
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
        return Component.translatable("blockEntity.planting_unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PlantingUnitScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }

    @Override
    protected Optional<PlantingUnitRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(2);
        for (int i = 0; i < 2; i++) {
            inv.setItem(i, this.itemStackHandler.getStackInSlot(i));
        }
        // 按当前模式过滤：物品模式只匹配纯物品配方，流体模式只匹配"种子+流体"配方
        List<PlantingUnitRecipe> matches = new ArrayList<>();
        for (Recipe<Container> candidate : world.getRecipeManager()
                .getRecipesFor(PlantingUnitRecipe.Type.INSTANCE, inv, world)) {
            if (candidate instanceof PlantingUnitRecipe recipe) {
                matches.add(recipe);
            }
        }
        for (PlantingUnitRecipe recipe : matches) {
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
            itemStackHandler.extractItem(INPUT_SLOT, 1, false);
            
            if (r.requiresFluid()) {
                fluidTank.drain(r.getRequiredFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        });
    }

    @Override
    protected boolean hasCorrectRecipe(Level world) {
        Optional<PlantingUnitRecipe> match = getMatchRecipe(world);

        if (match.isPresent()) {
            PlantingUnitRecipe recipe = match.get();
            ItemStack stack = itemStackHandler.getStackInSlot(INPUT_SLOT);
            if (!recipe.getInput().test(stack)) {
                return false;
            }
            
            if (recipe.requiresFluid()
                    && !hasEnoughFluid(recipe.getRequiredFluid(), recipe.getRequiredFluidAmount())) {
                return false;
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

    private record InputItemHandler(ItemStackHandler parent) implements IItemHandler {

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return parent.getStackInSlot(PlantingUnitBlockEntity.INPUT_SLOT);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack current = parent.getStackInSlot(PlantingUnitBlockEntity.INPUT_SLOT);
            if (current.isEmpty() || ItemStack.isSameItemSameTags(current, stack)) {
                return parent.insertItem(PlantingUnitBlockEntity.INPUT_SLOT, stack, simulate);
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
            return parent.getStackInSlot(PlantingUnitBlockEntity.OUTPUT_SLOT);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return parent.extractItem(PlantingUnitBlockEntity.OUTPUT_SLOT, amount, simulate);
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
