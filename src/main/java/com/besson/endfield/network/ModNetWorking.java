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
        CHANNEL.registerMessage(nextId(), SyncStoragePacket.class, SyncStoragePacket::encode, SyncStoragePacket::decode, SyncStoragePacket::handle);
        CHANNEL.registerMessage(nextId(), OpenStoragePacket.class, OpenStoragePacket::encode, OpenStoragePacket::new, OpenStoragePacket::handle);
        CHANNEL.registerMessage(nextId(), RequestItemPacket.class, RequestItemPacket::encode, RequestItemPacket::decode, RequestItemPacket::handle);
        CHANNEL.registerMessage(nextId(), DepositItemPacket.class, DepositItemPacket::encode, DepositItemPacket::decode, DepositItemPacket::handle);
        CHANNEL.registerMessage(nextId(), FillBucketFromPumpPacket.class, FillBucketFromPumpPacket::encode, FillBucketFromPumpPacket::decode, FillBucketFromPumpPacket::handle);
        CHANNEL.registerMessage(nextId(), TankBottleTransferPacket.class, TankBottleTransferPacket::encode, TankBottleTransferPacket::decode, TankBottleTransferPacket::handle);
        CHANNEL.registerMessage(nextId(), CrucibleSetOutputPacket.class, CrucibleSetOutputPacket::encode, CrucibleSetOutputPacket::decode, CrucibleSetOutputPacket::handle);
        CHANNEL.registerMessage(nextId(), CrucibleClearOutputPacket.class, CrucibleClearOutputPacket::encode, CrucibleClearOutputPacket::decode, CrucibleClearOutputPacket::handle);
        CHANNEL.registerMessage(nextId(), FluidModeChangePacket.class, FluidModeChangePacket::encode, FluidModeChangePacket::decode, FluidModeChangePacket::handle);
    }

}
