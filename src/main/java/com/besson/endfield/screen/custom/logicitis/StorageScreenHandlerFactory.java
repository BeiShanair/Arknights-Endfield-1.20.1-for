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

import java.util.ArrayList;
import java.util.List;

public class StorageScreenHandlerFactory implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.literal("Global Storage");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        StorageScreenHandler handler = new StorageScreenHandler(syncId, playerInventory);

        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel world = serverPlayer.serverLevel();
            StorageState state = GlobalStorageManager.get(world).getState();

            List<StorageEntry> stateEntries = new ArrayList<>(state.getStorage().values());

            for (StorageEntry e : stateEntries) {
                StorageEntry copy = new StorageEntry(e.getItem(), e.getCapacity());
                copy.insert(e.getCount());
                handler.getEntries().add(copy);
            }
            GlobalStorageManager manager = GlobalStorageManager.get(world);
            manager.addListener(serverPlayer);
            handler.refreshSlots();
        }

        return handler;
    }
}
