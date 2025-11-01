package com.besson.endfield.item.custom;


import com.besson.endfield.entity.ModItemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IndustrialExplosiveItem extends Item {
    public IndustrialExplosiveItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide()) {
            IndustrialExplosiveEntity explosive = new IndustrialExplosiveEntity(ModItemEntity.INDUSTRIAL_EXPLOSIVE.get(), pPlayer, pLevel);
            explosive.setItem(stack);

            float v = 1.5f;
            float i = 1.0f;

            explosive.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0f, v, i);

            pLevel.addFreshEntity(explosive);
        }
        pPlayer.getCooldowns().addCooldown(this, 20);
        if (!pPlayer.getAbilities().instabuild) {
            stack.shrink(1);
        }
        pPlayer.swing(pUsedHand);
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }
}
