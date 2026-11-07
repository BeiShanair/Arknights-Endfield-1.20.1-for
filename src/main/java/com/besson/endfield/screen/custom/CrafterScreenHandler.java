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
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CrafterScreenHandler extends AbstractContainerMenu {
    private static final int INPUT_SLOT_COUNT = 3;
    private static final int OUTPUT_SLOT_INDEX = 3;
    private static final int PLAYER_INVENTORY_START = 4;
    private static final int HOTBAR_END = 40;

    private final Container inventory;
    private final Container outputInventory;
    private final CrafterBlockEntity entity;
    private final Player player;

    private final Slot outputSlot;
    private final DataSlot selectedRecipe = DataSlot.standalone();
    private Runnable contentsChangedListener = () -> {};
    private List<CrafterRecipe> currentRecipes = new ArrayList<>();

    public CrafterScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, (CrafterBlockEntity) playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()));
    }

    public CrafterScreenHandler(int syncId, Inventory playerInventory, CrafterBlockEntity blockEntity) {
        super(ModScreens.CRAFTER_SCREEN.get(), syncId);
        checkContainerSize(blockEntity.getContainer(), INPUT_SLOT_COUNT);
        this.inventory = blockEntity.asContainer();
        inventory.startOpen(playerInventory.player);
        this.entity = blockEntity;
        this.player = playerInventory.player;
        this.outputInventory = new SimpleContainer(1);
        this.selectedRecipe.set(-1);

        this.addSlot(new Slot(inventory, 0, 26, 17) {
            @Override
            public void setChanged() {
                super.setChanged();
                slotsChanged(inventory);
            }
        });
        this.addSlot(new Slot(inventory, 1, 26, 35) {
            @Override
            public void setChanged() {
                super.setChanged();
                slotsChanged(inventory);
            }
        });
        this.addSlot(new Slot(inventory, 2, 26, 53) {
            @Override
            public void setChanged() {
                super.setChanged();
                slotsChanged(inventory);
            }
        });
        this.outputSlot = this.addSlot(new CrafterResultSlot(player, inventory, outputInventory, 0, 124, 35, this));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.addDataSlot(selectedRecipe);
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
            Item item = originalStack.getItem();
            newStack = originalStack.copy();

            if (invSlot == OUTPUT_SLOT_INDEX) {
                item.onCraftedBy(originalStack, player.level(), player);
                if (!this.moveItemStackTo(originalStack, PLAYER_INVENTORY_START, HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(originalStack, newStack);
            } else if (invSlot >= PLAYER_INVENTORY_START && invSlot < HOTBAR_END) {
                if (!this.moveItemStackTo(originalStack, 0, INPUT_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, PLAYER_INVENTORY_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, originalStack);
            this.broadcastChanges();
        }
        return newStack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.outputInventory && super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);
        if (pContainer == this.inventory) {
            this.updateResult();
            this.contentsChangedListener.run();
        }
    }

    public void updateResult(boolean resetSelection) {
        Level world = player.level();

        currentRecipes = world.getRecipeManager()
                .getRecipesFor(CrafterRecipe.Type.INSTANCE, inventory, world);
        if (resetSelection) {
            selectedRecipe.set(-1);
        } else if (!isSelectedRecipeInBounds()) {
            selectedRecipe.set(-1);
        }

        populateResult();
    }

    public void updateResult() {
        this.updateResult(false);
    }

    private void populateResult() {
        if (isSelectedRecipeInBounds()) {
            ItemStack result = currentRecipes.get(selectedRecipe.get()).assemble(inventory, player.level().registryAccess());
            outputSlot.set(result);
        } else {
            outputSlot.set(ItemStack.EMPTY);
        }

        outputInventory.setChanged();
        this.broadcastChanges();
    }
    
    public void changeRecipe() {
        if (currentRecipes.isEmpty()) {
            return;
        }

        int next = isSelectedRecipeInBounds() ? (selectedRecipe.get() + 1) % currentRecipes.size() : 0;
        selectRecipe(next);
    }

    public boolean selectRecipe(int index) {
        if (!isRecipeInBounds(index)) {
            return false;
        }

        selectedRecipe.set(index);
        populateResult();
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        return selectRecipe(id);
    }

    private boolean isRecipeInBounds(int index) {
        return index >= 0 && index < currentRecipes.size();
    }

    private boolean isSelectedRecipeInBounds() {
        return isRecipeInBounds(selectedRecipe.get());
    }


    @Override
    public boolean stillValid(Player pPlayer) {
        return this.inventory.stillValid(pPlayer);
    }

    public List<CrafterRecipe> getCurrentRecipes() {
        return currentRecipes;
    }

    public int getSelectedRecipeIndex() {
        return selectedRecipe.get();
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if (!pPlayer.level().isClientSide()) {
            this.outputInventory.removeItemNoUpdate(0);
            this.clearContainer(pPlayer, this.inventory);
        }
    }

    public int getAvailableRecipeCount() {
        return currentRecipes.size();
    }

    public boolean canCraft() {
        return !currentRecipes.isEmpty();
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }
    
}
