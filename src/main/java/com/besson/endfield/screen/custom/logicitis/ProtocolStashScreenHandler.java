package com.besson.endfield.screen.custom.logicitis;

import com.besson.endfield.blockEntity.custom.logicitis.ProtocolStashBlockEntity;
import com.besson.endfield.screen.ModScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ProtocolStashScreenHandler extends AbstractContainerMenu {
    public final ProtocolStashBlockEntity entity;

    private final ContainerData propertyDelegate;
    
    public ProtocolStashScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()),
                new SimpleContainerData(1));
    }
    public ProtocolStashScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.PROTOCOL_STASH_SCREEN.get(), syncId);
        this.entity = (ProtocolStashBlockEntity) blockEntity;
        this.propertyDelegate = propertyDelegate;

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 9; k++) {
                    this.addSlot(new SlotItemHandler(iItemHandler, k + j * 9, 8 + k * 18, j * 18 + 18));
                }
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
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
    public ItemStack quickMoveStack(Player pPlayer, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            int containerSlots = 27;
            if (invSlot < containerSlots) {
                if (!this.moveItemStackTo(originalStack, containerSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, containerSlots, false)) {
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


    public boolean isEnabled() {
        return propertyDelegate.get(0) == 1;
    }

    public void setEnabled(boolean enabled) {
        propertyDelegate.set(0, enabled ? 1 : 0);
    }
    
    @Override
    public boolean stillValid(Player pPlayer) {
        return entity != null;
    }
}
