package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.recipe.custom.OreRigRecipe;
import com.besson.endfield.screen.custom.PortableOriginiumRigScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
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

public class PortableOriginiumRigBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean isWorking;
    private static final int OUTPUT_SLOT = 0;

    protected final ContainerData propertyDelegate;
    private int progress = 0;
    private int maxProgress = 40;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<ItemStackHandler> lazyOptional = LazyOptional.of(() -> itemStackHandler);

    public PortableOriginiumRigBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTABLE_ORIGINIUM_RIG.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> PortableOriginiumRigBlockEntity.this.progress;
                    case 1 -> PortableOriginiumRigBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> PortableOriginiumRigBlockEntity.this.progress = pValue;
                    case 1 -> PortableOriginiumRigBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyOptional = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyOptional.invalidate();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0,
                state -> this.isWorking
                        ? state.setAndContinue(RawAnimation.begin().thenLoop("working"))
                        : state.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", itemStackHandler.serializeNBT());
        pTag.putInt("portable_originium_rig.progress", progress);
        pTag.putBoolean("portable_originium_rig.isWorking", isWorking);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        this.progress = pTag.getInt("portable_originium_rig.progress");
        this.isWorking = pTag.getBoolean("portable_originium_rig.isWorking");
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.portable_originium_rig");
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void tick(Level world, BlockPos pos, BlockState state, PortableOriginiumRigBlockEntity entity) {
        if (world.isClientSide()) {
            return;
        }

        boolean activeNow = this.hasCorrectRecipe(world);

        if (this.isOutputSlotAvailable()) {
            if (activeNow) {

                this.incrementProgress();
                setChanged(world, pos, state);

                if (this.hasCraftFinished()) {
                    this.craftItem(world);
                    this.resetProgress();
                }
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
            setChanged(world, pos, state);
        }

        if (entity.isWorking != activeNow) {
            entity.isWorking = activeNow;
            entity.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem(Level world) {

        Optional<OreRigRecipe> match = getMatchRecipe(world);

        if (match.isPresent()) {
            ItemStack result = match.get().getResultItem(world.registryAccess());
            ItemStack outputStack = itemStackHandler.getStackInSlot(OUTPUT_SLOT);
            itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), outputStack.getCount() + result.getCount()));
        }

    }

    private Optional<OreRigRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(1);
        BlockState belowState = world.getBlockState(this.getBlockPos().below());
        ItemStack belowStack = belowState.getBlock().asItem().getDefaultInstance();
        inv.setItem(0, belowStack);

        return world.getRecipeManager()
                .getRecipeFor(OreRigRecipe.Type.INSTANCE, inv, world)
                .map(recipe -> (OreRigRecipe) recipe);
    }

    private boolean hasCraftFinished () {
        return this.progress >= this.maxProgress;
    }

    private void incrementProgress() {
        this.progress++;
    }

    private boolean hasCorrectRecipe(Level world) {

        Optional<OreRigRecipe> match = getMatchRecipe(world);

        if (match.isPresent()) {
            ItemStack result = match.get().getResultItem(world.registryAccess());
            return canInsertItem(result);
        }
        return false;
    }

    private boolean canInsertItem(ItemStack item) {
        ItemStack outputStack = itemStackHandler.getStackInSlot(OUTPUT_SLOT);
        return outputStack.isEmpty() || (outputStack.getItem() == item.getItem()
                && outputStack.getCount() + item.getCount() <= outputStack.getMaxStackSize());
    }

    private boolean isOutputSlotAvailable() {
        ItemStack outputStack = itemStackHandler.getStackInSlot(OUTPUT_SLOT);
        return outputStack.isEmpty() || outputStack.getCount() < outputStack.getMaxStackSize();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PortableOriginiumRigScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }

    public ContainerData getPropertyDelegate() {
        return propertyDelegate;
    }

    public NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
        for (int i = 0; i < items.size(); i++) {
            items.set(i, itemStackHandler.getStackInSlot(i));
        }
        return items;
    }
}
