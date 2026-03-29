package com.besson.endfield.blockEntity.custom.resourcing;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.recipe.custom.OreRigRecipe;
import com.besson.endfield.screen.custom.PortableOriginiumRigScreenHandler;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class PortableOriginiumRigBlockEntity extends BaseRigBlockEntity<OreRigRecipe> implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public PortableOriginiumRigBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTABLE_ORIGINIUM_RIG.get(), pos, state, 60);
    }

    @Override
    protected ContainerData createPropertyDelegate() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> PortableOriginiumRigBlockEntity.this.progress;
                    case 1 -> PortableOriginiumRigBlockEntity.this.maxProgress;
                    case 2 -> PortableOriginiumRigBlockEntity.this.enable ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> PortableOriginiumRigBlockEntity.this.progress = value;
                    case 1 -> PortableOriginiumRigBlockEntity.this.maxProgress = value;
                    case 2 -> PortableOriginiumRigBlockEntity.this.enable = value == 1;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    protected int getPowerCostPerTick() {
        return 0;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 3, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0,
                state -> this.isWorking
                        ? state.setAndContinue(RawAnimation.begin().thenLoop("working"))
                        : state.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.portable_originium_rig");
    }

    public static void tick(Level world, BlockPos pos, BlockState state, PortableOriginiumRigBlockEntity be) {
        if (world.isClientSide()) return;

        if (!be.getEnable()) {
            be.isWorking = false;
            world.sendBlockUpdated(pos, state, state, 3);
            be.setChanged();
            return;
        }

        boolean activeNow = be.hasCorrectRecipe(world);

        if (be.isOutputSlotAvailable()) {
            if (activeNow) {

                be.incrementProgress();
                setChanged(world, pos, state);

                if (be.hasCraftingFinished()) {
                    be.craftItem(world);
                    be.resetProgress();
                }
            } else {
                be.resetProgress();
            }
        } else {
            be.resetProgress();
            be.setChanged();
        }

        if (be.isWorking != activeNow) {
            be.isWorking = activeNow;
            be.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }
    
    @Override
    protected void craftItem(Level world) {
        getMatchRecipe(world).ifPresent(r -> {
            ItemStack result = r.getResultItem(world.registryAccess());
            ItemStack outputStack = itemStackHandler.getStackInSlot(0);
            itemStackHandler.setStackInSlot(0,
                    new ItemStack(result.getItem(), outputStack.getCount() + result.getCount()));
        });
    }

    @Override
    protected Optional<OreRigRecipe> getMatchRecipe(Level world) {
        SimpleContainer inv = new SimpleContainer(1);
        BlockState belowState = world.getBlockState(this.getBlockPos().below());
        ItemStack belowStack = belowState.getBlock().asItem().getDefaultInstance();
        inv.setItem(0, belowStack);

        return world.getRecipeManager()
                .getRecipeFor(OreRigRecipe.Type.INSTANCE, inv, world)
                .map(recipe -> (OreRigRecipe) recipe);
    }

    @Override
    protected boolean hasCorrectRecipe(Level world) {
        return getMatchRecipe(world)
                .map(recipe -> {
                    if (recipe.getTier() > getTier()) return false;
                    return canOutputAccept(recipe.getResultItem(world.registryAccess()));
                })
                .orElse(false);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PortableOriginiumRigScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }

    @Override
    protected int getTier() {
        return 1;
    }
}
