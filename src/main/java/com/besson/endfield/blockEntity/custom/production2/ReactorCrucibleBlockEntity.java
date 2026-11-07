package com.besson.endfield.blockEntity.custom.production2;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.screen.custom.production2.ReactorCrucibleScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ReactorCrucibleBlockEntity extends BaseCrucibleBlockEntity {
    public static final int RESOURCE_SLOT_COUNT = 5;
    public static final int POWER_COST_PER_TICK = 20;

    public ReactorCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REACTOR_CRUCIBLE.get(), pos, state, RESOURCE_SLOT_COUNT, POWER_COST_PER_TICK);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.reactor_crucible");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        return new ReactorCrucibleScreenHandler(syncId, inventory, this);
    }
}
