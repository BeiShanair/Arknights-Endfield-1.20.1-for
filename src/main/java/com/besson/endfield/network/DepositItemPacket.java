package com.besson.endfield.network;

import com.besson.endfield.screen.custom.logicitis.StorageScreenHandler;
import com.besson.endfield.util.storage.GlobalStorageManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DepositItemPacket {
    private final Item item;
    private final CompoundTag tag;
    private final int amount;

    public DepositItemPacket(Item item, CompoundTag tag, int amount) {
        this.item = item;
        this.tag = tag;
        this.amount = amount;
    }

    public static DepositItemPacket decode(FriendlyByteBuf buf) {
        Item item = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
        CompoundTag tag = buf.readNbt();
        int amount = buf.readInt();
        return new DepositItemPacket(item, tag, amount);
    }

    public static void encode(DepositItemPacket msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(ForgeRegistries.ITEMS.getKey(msg.item));
        buf.writeNbt(msg.tag);
        buf.writeInt(msg.amount);
    }

    public static void handle(DepositItemPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || msg.item == null || msg.amount <= 0) return;
            
            if (!(player.containerMenu instanceof StorageScreenHandler)) return;

            GlobalStorageManager manager = GlobalStorageManager.get(player.serverLevel());

            long free = manager.getFreeSpace(msg.item, msg.tag);
            if (free <= 0) return;
            int want = (int) Math.min((long) msg.amount, free);
            
            int removed = GlobalStorageManager.removeFromInventory(
                    player.getInventory(), msg.item, msg.tag, want);
            if (removed <= 0) return;

            ItemStack toInsert = new ItemStack(msg.item, removed);
            if (msg.tag != null) toInsert.setTag(msg.tag);
            manager.insert(toInsert);

            player.getInventory().setChanged();
            player.containerMenu.broadcastChanges();
        });
        ctx.get().setPacketHandled(true);
    }
}
