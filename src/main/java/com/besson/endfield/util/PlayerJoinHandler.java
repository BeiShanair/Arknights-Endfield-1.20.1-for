package com.besson.endfield.util;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArknightsEndfield.MOD_ID)
public class PlayerJoinHandler {
    private static final String DATA_KEY = "protocol_anchor_core";
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag data = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

        if (!data.contains(DATA_KEY)) {
            ItemStack core = new ItemStack(ModBlocks.PROTOCOL_ANCHOR_CORE.get());
            if (!player.getInventory().add(core)) {
                player.drop(core, false);
            }
        }

        data.putBoolean(DATA_KEY, true);
        persistentData.put(Player.PERSISTED_NBT_TAG, data);
    }
}
