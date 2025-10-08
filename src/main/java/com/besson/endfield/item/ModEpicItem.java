package com.besson.endfield.item;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModEpicItem extends Item {

    public ModEpicItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatable(this.getDescriptionId(pStack)).withStyle(ChatFormatting.GOLD);
    }
}
