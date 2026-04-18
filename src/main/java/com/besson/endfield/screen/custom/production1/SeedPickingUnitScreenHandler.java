package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.blockEntity.custom.production1.SeedPickingUnitBlockEntity;
import com.besson.endfield.screen.ModScreens;
import com.besson.endfield.screen.custom.BaseIOScreenHandler;
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

public class SeedPickingUnitScreenHandler extends BaseIOScreenHandler<SeedPickingUnitBlockEntity> {
    public SeedPickingUnitScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, packetByteBuf)),
                new SimpleContainerData(3));
    }

    public SeedPickingUnitScreenHandler(int syncId, Inventory playerInventory, SeedPickingUnitBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.SEED_PICKING_UNIT_SCREEN.get(), syncId, playerInventory, blockEntity, propertyDelegate, 2);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 80, 11));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 80, 59));
        });
    }

    private static SeedPickingUnitBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof SeedPickingUnitBlockEntity e ? e : null;
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            int containerSlots = 1;
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
}
