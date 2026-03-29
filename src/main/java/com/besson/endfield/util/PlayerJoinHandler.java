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
    private static final String ORIGINIUM_ORE_KEY = "originium_ore";
    private static final String FERRIUM_ORE_KEY = "ferrium_ore";
    private static final String AMETHYST_ORE_KEY = "amethyst_ore";
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
        
        if (!data.contains(ORIGINIUM_ORE_KEY)) {
            ItemStack ore = new ItemStack(ModBlocks.ORIGINIUM_MINERAL_VEIN_BLOCK.get(), 10);
            if (!player.getInventory().add(ore)) {
                player.drop(ore, false);
            }
        }
        data.putBoolean(ORIGINIUM_ORE_KEY, true);
        persistentData.put(Player.PERSISTED_NBT_TAG, data);
        
        if (!data.contains(FERRIUM_ORE_KEY)) {
            ItemStack ore = new ItemStack(ModBlocks.FERRIUM_MINERAL_VEIN_BLOCK.get(), 10);
            if (!player.getInventory().add(ore)) {
                player.drop(ore, false);
            }
        }
        data.putBoolean(FERRIUM_ORE_KEY, true);
        persistentData.put(Player.PERSISTED_NBT_TAG, data);
        
        if (!data.contains(AMETHYST_ORE_KEY)) {
            ItemStack ore = new ItemStack(ModBlocks.AMETHYST_MINERAL_VEIN_BLOCK.get(), 10);
            if (!player.getInventory().add(ore)) {
                player.drop(ore, false);
            }
        }
        data.putBoolean(AMETHYST_ORE_KEY, true);
        persistentData.put(Player.PERSISTED_NBT_TAG, data);
    }
}
