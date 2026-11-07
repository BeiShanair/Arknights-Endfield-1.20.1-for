package com.besson.endfield.screen.custom.production1;

import com.besson.endfield.blockEntity.custom.production1.WaterTreatmentUnitBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class WaterTreatmentUnitScreenHandler extends AbstractContainerMenu {
    private static final int PROGRESS_ARROW_SIZE = 26;

    public final WaterTreatmentUnitBlockEntity entity;
    private final ContainerData containerData;

    public WaterTreatmentUnitScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, Objects.requireNonNull(getClientEntity(playerInventory, buf)),
                new SimpleContainerData(8));
    }

    public WaterTreatmentUnitScreenHandler(int syncId, Inventory playerInventory,
                                           WaterTreatmentUnitBlockEntity blockEntity, ContainerData containerData) {
        super(ModScreens.WATER_TREATMENT_UNIT_SCREEN.get(), syncId);
        this.entity = blockEntity;
        this.containerData = containerData;
        this.addDataSlots(containerData);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private static WaterTreatmentUnitBlockEntity getClientEntity(Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        BlockEntity be = playerInventory.player.level().getBlockEntity(pos);
        return be instanceof WaterTreatmentUnitBlockEntity e ? e : null;
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
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.entity != null
                && this.entity.getLevel() != null
                && this.entity.getBlockPos().closerThan(player.getOnPos(), 8);
    }

    public boolean isCrafting() {
        return containerData.get(0) > 0;
    }

    public boolean isEnabled() {
        return containerData.get(2) == 1;
    }

    public void setEnabled(boolean enabled) {
        containerData.set(2, enabled ? 1 : 0);
    }

    public int getScaledProgress() {
        int progress = this.containerData.get(0);
        int maxProgress = this.containerData.get(1);
        return maxProgress != 0 && progress != 0 ? progress * PROGRESS_ARROW_SIZE / maxProgress : 0;
    }

    public int getFluidAmount() {
        return containerData.get(3);
    }

    public int getFluidCapacity() {
        return containerData.get(4);
    }

    public int getStoredPower() {
        return containerData.get(5);
    }

    public boolean isWorking() {
        return containerData.get(6) == 1;
    }
    
    public int getFluidId() {
        return containerData.get(7);
    }
}
