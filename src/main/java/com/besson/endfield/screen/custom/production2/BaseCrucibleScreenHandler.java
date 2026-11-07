package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BaseCrucibleScreenHandler extends AbstractContainerMenu {
    private final BaseCrucibleBlockEntity blockEntity;

    protected BaseCrucibleScreenHandler(MenuType<?> type, int syncId,
                                        Inventory playerInventory, BaseCrucibleBlockEntity blockEntity) {
        super(type, syncId);
        this.blockEntity = blockEntity;
    }

    protected static @Nullable BaseCrucibleBlockEntity readCrucibleBlockEntity(Inventory inventory, FriendlyByteBuf buf) {
        var pos = buf.readBlockPos();
        BlockEntity be = inventory.player.level().getBlockEntity(pos);
        return be instanceof BaseCrucibleBlockEntity crucible ? crucible : null;
    }

    public BaseCrucibleBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.getLevel() == player.level()
                && player.distanceToSqr(blockEntity.getBlockPos().getX() + 0.5,
                blockEntity.getBlockPos().getY() + 0.5,
                blockEntity.getBlockPos().getZ() + 0.5) <= 64.0;
    }
}
