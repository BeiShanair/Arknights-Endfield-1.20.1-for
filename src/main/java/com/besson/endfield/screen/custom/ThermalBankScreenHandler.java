package com.besson.endfield.screen.custom;

import com.besson.endfield.blockEntity.custom.powering.ThermalBankBlockEntity;
import com.besson.endfield.screen.ModScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class ThermalBankScreenHandler extends AbstractContainerMenu {
    private final ContainerData propertyDelegate;
    public final ThermalBankBlockEntity entity;

    public  ThermalBankScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, buf)),
                new SimpleContainerData(3));
    }

    public ThermalBankScreenHandler(int syncId, Inventory playerInventory, ThermalBankBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.THERMAL_BANK_SCREEN.get(), syncId);
        checkContainerSize(playerInventory, 1);
        this.propertyDelegate = propertyDelegate;
        this.entity = blockEntity;

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 104, 37));
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(propertyDelegate);
    }

    private static ThermalBankBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof ThermalBankBlockEntity e ? e : null;
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            int inv = this.entity.getItems().size();

            if (invSlot < inv) {
                if (!this.moveItemStackTo(originalStack, inv, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, inv, false)) {
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
    public boolean stillValid(Player player) {
        return this.entity != null
                && this.entity.getLevel() != null
                && this.entity.getBlockPos().closerThan(player.getOnPos(), 8);
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
    public boolean isBurning(){
        return propertyDelegate.get(0) > 0;
    }
    public boolean isEnabled() {
        return propertyDelegate.get(2) == 1;
    }

    public void setEnabled(boolean enabled) {
        propertyDelegate.set(2, enabled ? 1 : 0);
    }
    public int getScaledProgress() {
        int burnTime = this.propertyDelegate.get(0);
        int fuelTime = this.propertyDelegate.get(1);
        int progressArrowSize = 14;
        return burnTime != 0 && fuelTime != 0 ? burnTime * progressArrowSize / fuelTime : 0;
    }
}
