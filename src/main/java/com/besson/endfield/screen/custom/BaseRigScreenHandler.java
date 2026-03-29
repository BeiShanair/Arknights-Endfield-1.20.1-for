package com.besson.endfield.screen.custom;

import com.besson.endfield.blockEntity.custom.resourcing.BaseRigBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class BaseRigScreenHandler<B extends BaseRigBlockEntity<?>> extends AbstractContainerMenu {
    protected final ContainerData propertyDelegate;
    protected final Level level;
    public final B entity;
    public BaseRigScreenHandler(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, B blockEntity, ContainerData propertyDelegate, int size) {
        super(type, syncId);
        checkContainerSize(playerInventory, size);
        this.propertyDelegate = propertyDelegate;
        this.entity = blockEntity;
        this.level = playerInventory.player.level();
        
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(propertyDelegate);
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

    public boolean isCrafting(){
        return propertyDelegate.get(0) > 0;
    }

    public boolean isEnabled() {
        return propertyDelegate.get(2) == 1;
    }

    public void setEnabled(boolean enabled) {
        propertyDelegate.set(2, enabled ? 1 : 0);
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
}
