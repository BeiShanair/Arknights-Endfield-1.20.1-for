package com.besson.endfield.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class FluidBottleItem extends BucketItem {
    private final Supplier<? extends Item> emptyBottle;

    public FluidBottleItem(Supplier<? extends Fluid> fluid, Supplier<? extends Item> emptyBottle, Properties properties) {
        super(fluid, properties);
        this.emptyBottle = emptyBottle;
    }
    
    public Item getEmptyBottle() {
        return emptyBottle.get();
    }
    
    public Fluid getBottledFluid() {
        return getFluid();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack original = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = super.use(level, player, hand);
        ItemStack resultStack = result.getObject();
        
        if (result.getResult().consumesAction() && resultStack != original && resultStack.is(Items.BUCKET)) {
            if (!level.isClientSide()) {
                ItemStack bottle = new ItemStack(getEmptyBottle());
                if (!player.getInventory().add(bottle)) {
                    player.drop(bottle, false);
                }
                original.shrink(1);
            }
            
            return InteractionResultHolder.consume(original);
        }
        return result;
    }
}
