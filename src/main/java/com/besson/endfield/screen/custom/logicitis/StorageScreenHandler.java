package com.besson.endfield.screen.custom.logicitis;

import com.besson.endfield.screen.ModScreens;
import com.besson.endfield.util.storage.GlobalStorageManager;
import com.besson.endfield.util.storage.StorageEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class StorageScreenHandler extends AbstractContainerMenu {
    private static final int STORAGE_ROWS = 6;
    private static final int STORAGE_COLS = 9;
    private static final int STORAGE_SLOT_COUNT = STORAGE_ROWS * STORAGE_COLS;

    private List<StorageEntry> entries;
    public int scrollOffset = 0;
    private final SimpleContainer storageInventory = new SimpleContainer(STORAGE_SLOT_COUNT);

    public StorageScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId,  playerInventory, null);
    }

    public StorageScreenHandler(int syncId, Inventory playerInv, FriendlyByteBuf buf) {
        super(ModScreens.STORAGE_SCREEN.get(), syncId);

        this.entries = new ArrayList<>();
        if (buf != null) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                ResourceLocation id = buf.readResourceLocation();
                Item item = ForgeRegistries.ITEMS.getValue(id);
                CompoundTag tag = buf.readNbt();
                long count = buf.readLong();

                if (item == null) continue;

                entries.add(new StorageEntry(item, tag, count, Math.max(count, 1L)));
            }

            for (int i = 0; i < Math.min(entries.size(), STORAGE_SLOT_COUNT); i++) {
                storageInventory.setItem(i, entries.get(i).toStack(1));
            }
        }

        int index = 0;
        for (int y = 0; y < STORAGE_ROWS; y++) {
            for (int x = 0; x < STORAGE_COLS; x++) {
                final int slotIndex = index;
                addSlot(new Slot(storageInventory, slotIndex, 8 + x * 18, 18 + y * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(Player pPlayer) {
                        return false;
                    }
                });
                index++;
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int playerSlotIndex = col + row * 9 + 9;
                addSlot(new Slot(playerInv, playerSlotIndex,
                        8 + col * 18,
                        103 + row * 18 + 36
                ));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col,
                    8 + col * 18,
                    197
            ));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public List<StorageEntry> getEntries() {
        return entries;
    }

    public List<StorageEntry> getVisibleEntries() {
        if (entries.isEmpty()) return List.of();
        int start = Mth.clamp(scrollOffset, 0, entries.size());
        int end = Math.min(start + STORAGE_SLOT_COUNT, entries.size());
        return entries.subList(start, end);
    }

    public void refreshSlots() {
        List<StorageEntry> visible = getVisibleEntries();

        int storageSlotsToUpdate = Math.min(STORAGE_SLOT_COUNT, slots.size());

        for (int i = 0; i < storageSlotsToUpdate; i++) {
            Slot slot = slots.get(i);
            if (i < visible.size()) {
                // 用带 NBT 的栈刷新，保证图标与提示都能体现变体差异
                slot.set(visible.get(i).toStack(1));
            } else {
                slot.set(ItemStack.EMPTY);
            }
        }
    }

    public int getStorageSlotCount() {
        return STORAGE_SLOT_COUNT;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if (pPlayer instanceof ServerPlayer serverPlayer) {
            GlobalStorageManager manager = GlobalStorageManager.get(serverPlayer.serverLevel());
            manager.removeListener(serverPlayer);
        }
    }

    public void updateEntries(List<StorageEntry> newEntries) {
        this.entries = newEntries;
        // 条目变少时把滚动位置拉回合法区间，避免 subList 越界
        int maxOffset = Math.max(0, entries.size() - STORAGE_SLOT_COUNT);
        this.scrollOffset = Mth.clamp(this.scrollOffset, 0, maxOffset);
        refreshSlots();
    }
}
