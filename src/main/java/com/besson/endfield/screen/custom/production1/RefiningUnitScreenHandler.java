package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.blockEntity.custom.production1.RefiningUnitBlockEntity;
import com.besson.endfield.screen.ModScreens;
import com.besson.endfield.screen.custom.BaseIOScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class RefiningUnitScreenHandler extends BaseIOScreenHandler<RefiningUnitBlockEntity> {
    private static final int MACHINE_SLOTS = 2;

    public RefiningUnitScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, packetByteBuf)),
                new SimpleContainerData(11));
    }

    public RefiningUnitScreenHandler(int syncId, Inventory playerInventory, RefiningUnitBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.REFINING_UNIT_SCREEN.get(), syncId, playerInventory, blockEntity, propertyDelegate, MACHINE_SLOTS);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, RefiningUnitBlockEntity.INPUT_SLOT, 80, 11));
            this.addSlot(new SlotItemHandler(iItemHandler, RefiningUnitBlockEntity.OUTPUT_SLOT, 80, 59) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        });
    }

    private static RefiningUnitBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof RefiningUnitBlockEntity e ? e : null;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            int machineStart = this.slots.size() - MACHINE_SLOTS;
            if (invSlot >= machineStart) {
                if (!this.moveItemStackTo(originalStack, 0, machineStart, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, machineStart, machineStart + 1, false)) {
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

    public boolean nowFluidMode() {
        return propertyDelegate.get(8) == 1;
    }

    public int getFluidAmount() {
        return propertyDelegate.get(3);
    }

    public int getFluidCapacity() {
        return propertyDelegate.get(4);
    }

    public int getSewageAmount() {
        return propertyDelegate.get(5);
    }

    public int getSewageCapacity() {
        return propertyDelegate.get(6);
    }

    public int getStoredPower() {
        return this.propertyDelegate.get(7);
    }
    
    public int getFluidId() {
        return this.propertyDelegate.get(9);
    }
    public int getSewageId() {
        return this.propertyDelegate.get(10);
    }
}
