package com.besson.endfield.blockEntity.custom.resourcing;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.recipe.custom.OreRigRecipe;
import com.besson.endfield.screen.custom.resourcing.HydroMiningRigScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class HydroMiningRigBlockEntity extends BaseRigBlockEntity<OreRigRecipe> implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final int WATER_CAPACITY = 50000;
    public static final int WATER_COST_PER_CRAFT = 1000;
    private static final int MAX_PROGRESS = 60;
    private final FluidTank waterTank = new FluidTank(WATER_CAPACITY,
            stack -> !stack.isEmpty() && stack.getFluid().isSame(Fluids.WATER)) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private LazyOptional<IFluidHandler> lazyWaterHandler = LazyOptional.of(() -> waterTank);

    public HydroMiningRigBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HYDRO_MINING_RIG.get(), pos, state, MAX_PROGRESS);
    }

    @Override
    protected ContainerData createPropertyDelegate() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> HydroMiningRigBlockEntity.this.progress;
                    case 1 -> HydroMiningRigBlockEntity.this.maxProgress;
                    case 2 -> HydroMiningRigBlockEntity.this.enable ? 1 : 0;
                    case 3 -> HydroMiningRigBlockEntity.this.waterTank.getFluidAmount();
                    case 4 -> WATER_CAPACITY;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }
    
    @Override
    protected int getPowerCostPerTick() {
        return 0;
    }

    @Override
    protected int getTier() {
        return 4;
    }

    @Override
    protected Optional<OreRigRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(1);
        BlockState below = world.getBlockState(this.getBlockPos().below());
        inv.setItem(0, below.getBlock().asItem().getDefaultInstance());

        return world.getRecipeManager()
                .getRecipeFor(OreRigRecipe.Type.INSTANCE, inv, world)
                .map(recipe -> (OreRigRecipe) recipe);
    }

    @Override
    protected boolean hasCorrectRecipe(Level world) {
        return getMatchRecipe(world)
                .map(recipe -> {
                    if (recipe.getTier() > getTier()) return false;
                    if (!hasEnoughWater()) return false;
                    return canOutputAccept(recipe.getResultItem(world.registryAccess()));
                })
                .orElse(false);
    }
    
    protected boolean hasEnoughWater() {
        return waterTank.getFluidAmount() >= WATER_COST_PER_CRAFT
                && waterTank.getFluid().getFluid().isSame(Fluids.WATER);
    }

    @Override
    protected void craftItem(Level world) {
        getMatchRecipe(world).ifPresent(recipe -> {
            ItemStack result = recipe.getResultItem(world.registryAccess());
            ItemStack outputStack = itemStackHandler.getStackInSlot(0);
            itemStackHandler.setStackInSlot(0,
                    new ItemStack(result.getItem(), outputStack.getCount() + result.getCount()));
            
            waterTank.drain(WATER_COST_PER_CRAFT, IFluidHandler.FluidAction.EXECUTE);
        });
    }
    
    public static void tick(Level world, BlockPos pos, BlockState state, HydroMiningRigBlockEntity be) {
        if (world.isClientSide()) return;

        if (!be.getEnable()) {
            be.isWorking = false;
            be.resetProgress();
            world.sendBlockUpdated(pos, state, state, 3);
            be.setChanged();
            return;
        }

        boolean canRun = be.isOutputSlotAvailable() && be.hasCorrectRecipe(world);

        if (canRun) {
            be.incrementProgress();
            if (be.hasCraftingFinished()) {
                be.craftItem(world);
                be.resetProgress();
            }
        } else {
            be.resetProgress();
        }

        be.submitTimer++;
        if (be.submitTimer >= SUBMIT_INTERVAL) {
            be.submitTimer = 0;
            be.flushToGlobalStorage(world);
        }

        if (be.isWorking != canRun) {
            be.isWorking = canRun;
            world.sendBlockUpdated(pos, state, state, 3);
        }

        be.setChanged();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyWaterHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyWaterHandler = LazyOptional.of(() -> waterTank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyWaterHandler.invalidate();
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(2, 4, 2);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("WaterTank", waterTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("WaterTank")) {
            waterTank.readFromNBT(tag.getCompound("WaterTank"));
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.hydro_mining_rig");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new HydroMiningRigScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
