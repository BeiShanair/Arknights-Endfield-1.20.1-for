package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.custom.GrindingUnitBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.recipe.InputEntry;
import com.besson.endfield.recipe.custom.GrindingUnitRecipe;
import com.besson.endfield.screen.custom.GrindingUnitScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class GrindingUnitBlockEntity extends BaseIOBlockEntity<GrindingUnitRecipe> implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final int INPUT_SLOT1 = 0;
    public static final int INPUT_SLOT2 = 1;
    public static final int OUTPUT_SLOT = 2;
    private static final int POWER_PRE_TICK = 10;

    public GrindingUnitBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRINDING_UNIT.get(), pos, state, 40);
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
    protected int getInvSize() {
        return 3;
    }

    @Override
    protected ContainerData createPropertyDelegate() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GrindingUnitBlockEntity.this.progress;
                    case 1 -> GrindingUnitBlockEntity.this.maxProgress;
                    case 2 -> GrindingUnitBlockEntity.this.enable ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GrindingUnitBlockEntity.this.progress = value;
                    case 1 -> GrindingUnitBlockEntity.this.maxProgress = value;
                    case 2 -> GrindingUnitBlockEntity.this.enable = value == 1;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
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
            Direction facing = getBlockState().getValue(GrindingUnitBlock.FACING);
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
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        input = LazyOptional.of(() -> new InputItemHandler(itemStackHandler));
        output = LazyOptional.of(() -> new OutputItemHandler(itemStackHandler));
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
        return Component.translatable("blockEntity.grinding_unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GrindingUnitScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }
    
    @Override
    protected Optional<GrindingUnitRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(3);
        for (int i = 0; i < 3; i++) {
            inv.setItem(i, this.itemStackHandler.getStackInSlot(i));
        }
        return world.getRecipeManager()
                .getRecipeFor(GrindingUnitRecipe.Type.INSTANCE, inv, world);
    }

    @Override
    protected void craftItem(Level world) {
        getMatchRecipe(world).ifPresent(r -> {
            ItemStack result = r.getResultItem(world.registryAccess());
            this.itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));

            NonNullList<InputEntry> recipeInputs = r.getInput();
            boolean[] used = new boolean[2];
            for (InputEntry entry: recipeInputs) {
                for (int i = 0; i < used.length; i++) {
                    ItemStack stack = this.itemStackHandler.getStackInSlot(i);
                    if (!used[i] && entry.getIngredient().test(stack) && stack.getCount() >= entry.getCount()) {
                        this.itemStackHandler.extractItem(i, entry.getCount(), false);
                        used[i] = true;
                        break;

                    }
                }
            }
        });
    }

    @Override
    protected boolean hasCorrectRecipe(Level world) {
        Optional<GrindingUnitRecipe> match = getMatchRecipe(world);
        if (match.isPresent()) {
            NonNullList<InputEntry> recipeInputs = match.get().getInput();
            boolean[] used = new boolean[2];
            for (InputEntry entry : recipeInputs) {
                boolean matched = false;
                for (int i = 0; i < used.length; i++) {
                    ItemStack stack = this.itemStackHandler.getStackInSlot(i);
                    if (!used[i] && entry.getIngredient().test(stack) && stack.getCount() >= entry.getCount()) {
                        matched = true;
                        used[i] = true;
                        break;
                    }
                }
                if (!matched) return false;
            }
            ItemStack result = match.get().getResultItem(world.registryAccess());
            return canOutputAccept(result);
        }
        return false;
    }

    private record InputItemHandler(ItemStackHandler parent) implements IItemHandler {

        @Override
        public int getSlots() {
            return 2;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot == INPUT_SLOT1) return parent.getStackInSlot(GrindingUnitBlockEntity.INPUT_SLOT1);
            if (slot == INPUT_SLOT2) return parent.getStackInSlot(GrindingUnitBlockEntity.INPUT_SLOT2);
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot == INPUT_SLOT1) {
                return parent.insertItem(GrindingUnitBlockEntity.INPUT_SLOT1, stack, simulate);
            } else if (slot == INPUT_SLOT2) {
                return parent.insertItem(GrindingUnitBlockEntity.INPUT_SLOT2, stack, simulate);
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
            return parent.getStackInSlot(GrindingUnitBlockEntity.OUTPUT_SLOT);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return parent.extractItem(GrindingUnitBlockEntity.OUTPUT_SLOT, amount, simulate);
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
