package com.besson.endfield.screen.custom.resourcing;

import com.besson.endfield.blockEntity.custom.resourcing.FluidPumpBlockEntity;
import com.besson.endfield.screen.ModScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FluidPumpScreenHandler extends AbstractContainerMenu {
    private final FluidPumpBlockEntity blockEntity;
    private final ContainerData containerData;

    public FluidPumpScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory,
                (FluidPumpBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(4));
    }

    public FluidPumpScreenHandler(int syncId, Inventory playerInventory, FluidPumpBlockEntity blockEntity, ContainerData containerData) {
        super(ModScreens.FLUID_PUMP_SCREEN.get(), syncId);
        this.blockEntity = blockEntity;
        this.containerData = containerData;
        this.addDataSlots(containerData);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public int getFluidAmount() {
        return this.containerData.get(0);
    }

    public int getFluidCapacity() {
        return this.containerData.get(1);
    }

    public int getStoredPower() {
        return this.containerData.get(2);
    }

    public int getFluidId() {
        return this.containerData.get(3);
    }

    public BlockPos getBlockEntityPos() {
        return this.blockEntity.getBlockPos();
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
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
