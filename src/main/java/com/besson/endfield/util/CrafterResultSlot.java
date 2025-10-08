package com.besson.endfield.util;


import com.besson.endfield.recipe.custom.CrafterRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CrafterResultSlot extends Slot {
    private final Container inputInventory;
    private final Player player;
    private final AbstractContainerMenu handler;

    public CrafterResultSlot(Player player, Container input, Container output, int index, int x, int y, AbstractContainerMenu handler) {
        super(output, index, x, y);
        this.player = player;
        this.inputInventory = input;
        this.handler = handler;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        Level world = player.level();
        if (!world.isClientSide()) {
            Optional<CrafterRecipe> match = world.getRecipeManager()
                    .getRecipeFor(CrafterRecipe.Type.INSTANCE, inputInventory, world);
            match.ifPresent(crafterRecipe -> crafterRecipe.assemble(inputInventory, world.registryAccess()));
        }
        handler.slotsChanged(inputInventory);
        super.onTake(player, stack);
    }
}
