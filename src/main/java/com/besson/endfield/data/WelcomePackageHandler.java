package com.besson.endfield.data;

import com.besson.endfield.ArknightsEndfield;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = ArknightsEndfield.MOD_ID)
public final class WelcomePackageHandler {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final String VERSION_KEY = "welcome_version";

	private WelcomePackageHandler() {}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		MinecraftServer server = player.getServer();
		if (server != null) {
			grantPending(player, server);
		}
	}
	
	public static int grantPending(ServerPlayer player, MinecraftServer server) {
		CompoundTag persistent = player.getPersistentData();
		CompoundTag data = persistent.getCompound(Player.PERSISTED_NBT_TAG);
		int current = data.getInt(VERSION_KEY);

		NavigableMap<Integer, List<JsonElement>> packages = WelcomePackages.get(server);
		int latest = packages.isEmpty() ? 0 : packages.lastKey();
		if (latest <= current) {
			return 0;
		}

		int given = 0;
		int dropped = 0;
		for (List<JsonElement> items : packages.tailMap(current, false).values()) {
			for (JsonElement json : items) {
				Optional<ItemStack> parsed = ItemStack.CODEC.parse(JsonOps.INSTANCE, json).result();
				if (parsed.isEmpty() || parsed.get().isEmpty()) {
					LOGGER.error("欢迎礼包物品解析失败（玩家 {}）：{}", player.getName().getString(), json);
					continue;
				}

				ItemStack stack = parsed.get();
				int droppedThis = deliver(player, stack);
				given += stack.getCount() - droppedThis;
				dropped += droppedThis;
			}
		}

		data.putInt(VERSION_KEY, latest);
		persistent.put(Player.PERSISTED_NBT_TAG, data);

		LOGGER.info("已向 {} 发放欢迎礼包 v{} -> v{}，共 {} 件物品（掉落 {} 件）",
				player.getName().getString(), current, latest, given, dropped);

		if (given > 0) {
			player.displayClientMessage(Component.translatable("message.arknights_endfield.welcome_package"), false);
		}
		if (dropped > 0) {
			player.displayClientMessage(
					Component.translatable("message.arknights_endfield.welcome_package_dropped", dropped), false);
		}
		return given;
	}
	
	private static int deliver(ServerPlayer player, ItemStack stack) {
		int count = stack.getCount();
		int toAdd = Math.min(count, freeSpaceFor(player, stack));

		if (toAdd > 0) {
			ItemStack part = stack.copyWithCount(toAdd);
			if (!player.getInventory().add(part) && !part.isEmpty()) {
				player.drop(part, false);
				toAdd -= part.getCount();
			}
		}

		int rest = count - toAdd;
		if (rest > 0) {
			player.drop(stack.copyWithCount(rest), false);
		}
		return rest;
	}
	
	private static int freeSpaceFor(ServerPlayer player, ItemStack stack) {
		Inventory inventory = player.getInventory();
		int max = stack.getMaxStackSize();
		int room = 0;

		for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
			if (slot >= 36 && slot <= 39) {
				continue;
			}
			ItemStack existing = inventory.getItem(slot);
			if (existing.isEmpty()) {
				room += max;
			} else if (ItemStack.isSameItemSameTags(existing, stack)) {
				room += Math.max(0, max - existing.getCount());
			}
		}
		return room;
	}
}
