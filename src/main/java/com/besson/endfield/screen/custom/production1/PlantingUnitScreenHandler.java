package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.blockEntity.custom.production1.PlantingUnitBlockEntity;
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

public class PlantingUnitScreenHandler extends BaseIOScreenHandler<PlantingUnitBlockEntity> {
    private static final int MACHINE_SLOTS = 2;

    public PlantingUnitScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, packetByteBuf)),
                new SimpleContainerData(8));
    }

    public PlantingUnitScreenHandler(int syncId, Inventory playerInventory, PlantingUnitBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.PLANTING_UNIT_SCREEN.get(), syncId, playerInventory, blockEntity, propertyDelegate, MACHINE_SLOTS);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, PlantingUnitBlockEntity.INPUT_SLOT, 80, 11));
            this.addSlot(new SlotItemHandler(iItemHandler, PlantingUnitBlockEntity.OUTPUT_SLOT, 80, 59) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        });
    }

    private static PlantingUnitBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof PlantingUnitBlockEntity e ? e : null;
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

    public int getStoredPower() {
        return propertyDelegate.get(3);
    }
    public boolean nowFluidMode() {
        return propertyDelegate.get(6) == 1;
    }
    public int getFluidAmount() {
        return propertyDelegate.get(4);
    }
    public int getFluidCapacity() {
        return propertyDelegate.get(5);
    }
    public int getFluidId() {
        return propertyDelegate.get(7);
    }
}
