package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.screen.custom.CrafterScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrafterBlockEntity extends BlockEntity implements MenuProvider {
    private final NonNullList<ItemStack> inv = NonNullList.withSize(4, ItemStack.EMPTY);

    public NonNullList<ItemStack> getInv() {
        return inv;
    }

    public SimpleContainer getContainer() {
        SimpleContainer container = new SimpleContainer(inv.size());
        for (int i = 0; i < inv.size(); i++) {
            container.setItem(i, inv.get(i));
        }
        return container;
    }

    public CrafterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRAFTER.get(), pos, state);
    }

    public NonNullList<ItemStack> getItems() {
        return inv;
    }

    public SimpleContainer asContainer() {
        SimpleContainer container = new SimpleContainer(inv.size());
        for (int i = 0; i < inv.size(); i++) {
            container.setItem(i, inv.get(i));
        }
        return container;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.crafter");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CrafterScreenHandler(pContainerId, pPlayerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

}
