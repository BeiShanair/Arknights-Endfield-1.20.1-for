package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.blockEntity.custom.production2.SeparatingUnitBlockEntity;
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

public class SeparatingUnitScreenHandler extends BaseIOScreenHandler<SeparatingUnitBlockEntity> {
    private static final int MACHINE_SLOTS = 2;

    public SeparatingUnitScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf) {
        this(syncId, inventory, Objects.requireNonNull(getClientEntity(inventory, buf)), new SimpleContainerData(7));
    }

    public SeparatingUnitScreenHandler(int syncId, Inventory inventory, SeparatingUnitBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.SEPARATING_UNIT_SCREEN.get(), syncId, inventory, blockEntity, propertyDelegate, MACHINE_SLOTS);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, SeparatingUnitBlockEntity.INPUT_SLOT, 80, 11));
            this.addSlot(new SlotItemHandler(handler, SeparatingUnitBlockEntity.OUTPUT_SLOT, 80, 59) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        });
    }

    private static SeparatingUnitBlockEntity getClientEntity(Inventory inventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = inventory.player.level().getBlockEntity(pos);
        return be instanceof SeparatingUnitBlockEntity e ? e : null;
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

    public int getFluidAmount() {
        return propertyDelegate.get(4);
    }

    public int getFluidCapacity() {
        return propertyDelegate.get(5);
    }

    public int getFluidRawId() {
        return propertyDelegate.get(6);
    }
}
