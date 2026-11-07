package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.blockEntity.custom.production2.ExpandedCrucibleBlockEntity;
import com.besson.endfield.screen.ModScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import java.util.Objects;

public class ExpandedCrucibleScreenHandler extends BaseCrucibleScreenHandler {
    public ExpandedCrucibleScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf) {
        this(syncId, inventory, Objects.requireNonNull(getClientEntity(inventory, buf)));
    }

    public ExpandedCrucibleScreenHandler(int syncId, Inventory inventory, ExpandedCrucibleBlockEntity blockEntity) {
        super(ModScreens.EXPANDED_CRUCIBLE_SCREEN.get(), syncId, inventory, blockEntity);
    }

    private static ExpandedCrucibleBlockEntity getClientEntity(Inventory inventory, FriendlyByteBuf buf) {
        return readCrucibleBlockEntity(inventory, buf) instanceof ExpandedCrucibleBlockEntity reactor ? reactor : null;
    }

    @Override
    public ExpandedCrucibleBlockEntity getBlockEntity() {
        return (ExpandedCrucibleBlockEntity) super.getBlockEntity();
    }
}
