package com.besson.endfield.network;

import com.besson.endfield.util.storage.GlobalStorageManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RequestItemPacket {
    private final Item item;
    private final int amount;

    public RequestItemPacket(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public static RequestItemPacket decode(FriendlyByteBuf buf) {
        Item item = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
        int amount = buf.readInt();
        return new RequestItemPacket(item, amount);
    }

    public static void encode(RequestItemPacket msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(ForgeRegistries.ITEMS.getKey(msg.item));
        buf.writeInt(msg.amount);
    }

    public static void handle(RequestItemPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            ItemStack extracted = GlobalStorageManager.get(level).extract(msg.item, msg.amount);
            if (!player.getInventory().add(extracted)) {
                player.drop(extracted, false);
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
