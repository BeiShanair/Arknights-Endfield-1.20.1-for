package com.besson.endfield.network;

import com.besson.endfield.ArknightsEndfield;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetWorking {
    private static final String PROTOCOL = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int packetId = 0;

    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        CHANNEL.registerMessage(nextId(), CycleRecipePacket.class, CycleRecipePacket::encode, CycleRecipePacket::new, CycleRecipePacket::handle);
        CHANNEL.registerMessage(nextId(), SwitchPacket.class, SwitchPacket::encode, SwitchPacket::decode, SwitchPacket::handle);
    }

}
