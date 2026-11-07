package com.besson.endfield.screen.custom.logicitis;

import com.besson.endfield.util.storage.GlobalStorageManager;
import com.besson.endfield.util.storage.StorageEntry;
import com.besson.endfield.util.storage.StorageState;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class StorageScreenHandlerFactory implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.global_storage");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        StorageScreenHandler handler = new StorageScreenHandler(syncId, playerInventory);

        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel world = serverPlayer.serverLevel();
            StorageState state = GlobalStorageManager.get(world).getState();

            // 深拷贝一份给界面，避免界面直接持有持久化数据；拷贝会带上各自的 NBT 变体
            for (StorageEntry entry : state.getEntries()) {
                handler.getEntries().add(new StorageEntry(entry));
            }

            GlobalStorageManager manager = GlobalStorageManager.get(world);
            manager.addListener(serverPlayer);
            handler.refreshSlots();
        }

        return handler;
    }
}
