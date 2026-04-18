package com.besson.endfield.screen.custom.logicitis;

import com.besson.endfield.blockEntity.custom.logicitis.DepotUnloaderBlockEntity;
import com.besson.endfield.screen.ModScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class DepotUnloaderScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final DepotUnloaderBlockEntity entity;

    public DepotUnloaderScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, buf)),
                new SimpleContainerData(1));
    }

    public DepotUnloaderScreenHandler(int syncId, Inventory playerInventory, DepotUnloaderBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.DEPOT_UNLOADER_SCREEN.get(), syncId);
        checkContainerSize(playerInventory, 1);
        this.inventory = blockEntity.getFilterInventory();
        this.propertyDelegate = propertyDelegate;
        this.entity = blockEntity;
        
        this.addSlot(new Slot(inventory, 0, 104, 37) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                entity.clearFilter();
            }

            @Override
            public void setChanged() {
                super.setChanged();
                DepotUnloaderScreenHandler.this.slotsChanged(inventory);
                if (!inventory.getItem(0).isEmpty()) {
                    entity.setFilter(inventory.getItem(0));
                } else {
                    entity.clearFilter();
                }
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(propertyDelegate);
    }
    
    private static DepotUnloaderBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof DepotUnloaderBlockEntity e ? e : null;
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
    public boolean stillValid(Player player) {
        return this.entity != null
                && this.entity.getLevel() != null
                && this.entity.getBlockPos().closerThan(player.getOnPos(), 8);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
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
}
