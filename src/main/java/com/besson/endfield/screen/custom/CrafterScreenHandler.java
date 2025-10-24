package com.besson.endfield.screen.custom;

import com.besson.endfield.blockEntity.custom.CrafterBlockEntity;
import com.besson.endfield.recipe.custom.CrafterRecipe;
import com.besson.endfield.screen.ModScreens;
import com.besson.endfield.util.CrafterResultSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class CrafterScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final Container outputInventory;
    private final CrafterBlockEntity entity;
    private final Player player;

    private List<CrafterRecipe> currentRecipes = List.of();
    private int selectedRecipeIndex = 0;

    public CrafterScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, (CrafterBlockEntity) playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()));
    }

    public CrafterScreenHandler(int syncId, Inventory playerInventory, CrafterBlockEntity blockEntity) {
        super(ModScreens.CRAFTER_SCREEN.get(), syncId);
        checkContainerSize(blockEntity.getContainer(), 4);
        this.inventory = blockEntity.asContainer();
        inventory.startOpen(playerInventory.player);
        this.entity = blockEntity;
        this.player = playerInventory.player;
        this.outputInventory = new SimpleContainer(1);

        this.addSlot(new Slot(inventory, 0, 30, 35) {
            @Override
            public void setChanged() {
                super.setChanged();
                slotsChanged(inventory);
            }
        });
        this.addSlot(new Slot(inventory, 1, 48, 35) {
            @Override
            public void setChanged() {
                super.setChanged();
                slotsChanged(inventory);
            }
        });
        this.addSlot(new Slot(inventory, 2, 66, 35) {
            @Override
            public void setChanged() {
                super.setChanged();
                slotsChanged(inventory);
            }
        });
        this.addSlot(new CrafterResultSlot(player, inventory, outputInventory, 0, 124, 35, this));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (invSlot == 3) {
                while (true) {
                    if (!slot.hasItem() || !this.canTakeItemForPickAll(originalStack, this.slots.get(3))) {
                        break;
                    }

                    ItemStack crafted = slot.getItem();
                    if (!this.moveItemStackTo(crafted, 4, 40, true)) {
                        break;
                    }
                    slot.onTake(player, originalStack);
                }
            } else if (invSlot >= 4 && invSlot < 40) {
                if (!this.moveItemStackTo(originalStack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);
        if (pContainer == this.inventory) {
            this.updateResult();
        }
    }

    public void updateResult() {
        Level world = player.level();
        if (world.isClientSide()) return;

        currentRecipes = world.getRecipeManager()
                .getRecipesFor(CrafterRecipe.Type.INSTANCE, inventory, world);
        if (!currentRecipes.isEmpty()) {
            if (selectedRecipeIndex >= currentRecipes.size()) {
                selectedRecipeIndex = 0;
            }
            ItemStack result = currentRecipes.get(selectedRecipeIndex).getResultItem(world.registryAccess());
            outputInventory.setItem(0, result.copy());
        } else {
            outputInventory.setItem(0, ItemStack.EMPTY);
        }
        outputInventory.setChanged();
    }

    public void changeRecipe() {
        if (!currentRecipes.isEmpty()) {
            selectedRecipeIndex = (selectedRecipeIndex + 1) % currentRecipes.size();
            updateResult();
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.inventory.stillValid(pPlayer);
    }

    public List<CrafterRecipe> getCurrentRecipes() {
        return currentRecipes;
    }

    public int getSelectedRecipeIndex() {
        return selectedRecipeIndex;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if (!pPlayer.level().isClientSide()) {
            this.clearContainer(pPlayer, this.inventory);
        }
    }
}
