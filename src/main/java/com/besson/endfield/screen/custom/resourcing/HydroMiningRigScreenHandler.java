package com.besson.endfield.screen.custom.resourcing;

import com.besson.endfield.blockEntity.custom.resourcing.HydroMiningRigBlockEntity;
import com.besson.endfield.screen.ModScreens;
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

public class HydroMiningRigScreenHandler extends BaseRigScreenHandler<HydroMiningRigBlockEntity> {
    public HydroMiningRigScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, buf)),
                new SimpleContainerData(5));
    }

    public HydroMiningRigScreenHandler(int syncId, Inventory playerInventory, HydroMiningRigBlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.HYDRO_MINING_RIG_SCREEN.get(), syncId, playerInventory, blockEntity, propertyDelegate, 1);

        // 输出槽：只能看/取，不能塞
        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler ->
                this.addSlot(new SlotItemHandler(handler, 0, 104, 37) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                }));
    }

    public int getWaterAmount() {
        return this.propertyDelegate.get(3);
    }

    public int getWaterCapacity() {
        return this.propertyDelegate.get(4);
    }

    private static HydroMiningRigBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof HydroMiningRigBlockEntity e ? e : null;
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
