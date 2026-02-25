package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.custom.SeedPickingUnitBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.recipe.custom.SeedPickingUnitRecipe;
import com.besson.endfield.screen.custom.SeedPickingUnitScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class SeedPickingUnitBlockEntity extends BaseIOBlockEntity<SeedPickingUnitRecipe> implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private static final int POWER_PRE_TICK = 10;

    public SeedPickingUnitBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEED_PICKING_UNIT.get(), pos, state, 40);
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
                    case 0 -> SeedPickingUnitBlockEntity.this.progress;
                    case 1 -> SeedPickingUnitBlockEntity.this.maxProgress;
                    case 2 -> SeedPickingUnitBlockEntity.this.enable ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SeedPickingUnitBlockEntity.this.progress = value;
                    case 1 -> SeedPickingUnitBlockEntity.this.maxProgress = value;
                    case 2 -> SeedPickingUnitBlockEntity.this.enable = value == 1;
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
            Direction facing = this.getBlockState().getValue(SeedPickingUnitBlock.FACING);
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
        controllers.add(new AnimationController<>(this, "controller", 0,
                state -> state.setAndContinue(RawAnimation.begin().thenLoop("working"))
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.seed_picking_unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SeedPickingUnitScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }
    
    @Override
    protected void craftItem(Level world) {
        getMatchRecipe(world).ifPresent(r -> {
            ItemStack result = r.getResultItem(world.registryAccess());
            this.itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
            this.itemStackHandler.extractItem(INPUT_SLOT, 1,false);
        });
    }

    @Override
    protected Optional<SeedPickingUnitRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(2);
        for (int i = 0; i < 2; i++) {
            inv.setItem(i, this.itemStackHandler.getStackInSlot(i));
        }
        return world.getRecipeManager()
                .getRecipeFor(SeedPickingUnitRecipe.Type.INSTANCE, inv, world);
    }

    @Override
    protected boolean hasCorrectRecipe(Level world) {
        Optional<SeedPickingUnitRecipe> match = getMatchRecipe(world);
        if (match.isPresent()) {
            ItemStack result = match.get().getResultItem(world.registryAccess());
            return canOutputAccept(result);
        }
        return false;
    }

    private record InputItemHandler(ItemStackHandler parent) implements IItemHandler {

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return parent.getStackInSlot(SeedPickingUnitBlockEntity.INPUT_SLOT);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack current = parent.getStackInSlot(SeedPickingUnitBlockEntity.INPUT_SLOT);
            if (current.isEmpty() || ItemStack.isSameItemSameTags(current, stack)) {
                return parent.insertItem(SeedPickingUnitBlockEntity.INPUT_SLOT, stack, simulate);
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
            return parent.getStackInSlot(SeedPickingUnitBlockEntity.OUTPUT_SLOT);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return parent.extractItem(SeedPickingUnitBlockEntity.OUTPUT_SLOT, amount, simulate);
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
