package com.besson.endfield.screen.custom;

import com.besson.endfield.blockEntity.custom.ProtocolAnchorCoreBlockEntity;
import com.besson.endfield.screen.ModScreens;
import com.besson.endfield.util.ProtocolAnchorCoreStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ProtocolAnchorCoreScreenHandler extends AbstractContainerMenu {
    private final ContainerData propertyDelegate;
    public final ProtocolAnchorCoreBlockEntity entity;
    private final BlockPos pos;
    public double supplyRatio;
    public int totalGenerated;
    public int totalDemand;
    public int storedEnergy;

    public ProtocolAnchorCoreScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(packetByteBuf.readBlockPos()),
                new SimpleContainerData(4));
        this.supplyRatio = packetByteBuf.readDouble();
        this.totalGenerated = packetByteBuf.readInt();
        this.totalDemand = packetByteBuf.readInt();
        this.storedEnergy = packetByteBuf.readInt();
    }
    public ProtocolAnchorCoreScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, ContainerData propertyDelegate) {
        super(ModScreens.PROTOCOL_ANCHOR_CORE_SCREEN.get(), syncId);
        this.propertyDelegate = propertyDelegate;
        this.entity = (ProtocolAnchorCoreBlockEntity) blockEntity;
        this.pos = this.entity.getBlockPos();

        addDataSlots(propertyDelegate);

    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return entity != null;
    }
}
