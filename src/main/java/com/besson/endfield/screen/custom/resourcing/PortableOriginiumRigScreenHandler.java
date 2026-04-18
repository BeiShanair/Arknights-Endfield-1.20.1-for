package com.besson.endfield.screen.custom.resourcing;

import com.besson.endfield.blockEntity.custom.resourcing.PortableOriginiumRigBlockEntity;
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

public class PortableOriginiumRigScreenHandler extends BaseRigScreenHandler<PortableOriginiumRigBlockEntity> {
    public PortableOriginiumRigScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, buf)), 
                new SimpleContainerData(3));
    }

    public PortableOriginiumRigScreenHandler(int syncId, Inventory playerInventory, PortableOriginiumRigBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.PORTABLE_ORIGINIUM_RIG_SCREEN.get(), syncId, playerInventory, blockEntity, propertyDelegate, 1);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 104, 37));
        });
    }

    private static PortableOriginiumRigBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof PortableOriginiumRigBlockEntity e ? e : null;
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
