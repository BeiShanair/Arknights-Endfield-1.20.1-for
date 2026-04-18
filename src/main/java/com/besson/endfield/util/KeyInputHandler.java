package com.besson.endfield.util;

import com.besson.endfield.network.ModNetWorking;
import com.besson.endfield.network.OpenStoragePacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyInputHandler {
    
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (ModKeyBindings.OPEN_STORAGE.consumeClick()) {
                sendOpenPacket();
            }
        }
    }

    private static void sendOpenPacket() {
        ModNetWorking.CHANNEL.sendToServer(new OpenStoragePacket());
    }
}
