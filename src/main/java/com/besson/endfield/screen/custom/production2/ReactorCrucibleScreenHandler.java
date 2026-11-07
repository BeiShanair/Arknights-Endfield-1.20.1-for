package com.besson.endfield.screen.custom.production2;

import com.besson.endfield.blockEntity.custom.production2.ReactorCrucibleBlockEntity;
import com.besson.endfield.screen.ModScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import java.util.Objects;

public class ReactorCrucibleScreenHandler extends BaseCrucibleScreenHandler {
    public ReactorCrucibleScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf) {
        this(syncId, inventory, Objects.requireNonNull(getClientEntity(inventory, buf)));
    }

    public ReactorCrucibleScreenHandler(int syncId, Inventory inventory, ReactorCrucibleBlockEntity blockEntity) {
        super(ModScreens.REACTOR_CRUCIBLE_SCREEN.get(), syncId, inventory, blockEntity);
    }

    private static ReactorCrucibleBlockEntity getClientEntity(Inventory inventory, FriendlyByteBuf buf) {
        return readCrucibleBlockEntity(inventory, buf) instanceof ReactorCrucibleBlockEntity reactor ? reactor : null;
    }

    @Override
    public ReactorCrucibleBlockEntity getBlockEntity() {
        return (ReactorCrucibleBlockEntity) super.getBlockEntity();
    }
}
