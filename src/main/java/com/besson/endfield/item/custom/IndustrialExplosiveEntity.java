package com.besson.endfield.item.custom;

import com.besson.endfield.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class IndustrialExplosiveEntity extends ThrowableItemProjectile {
    public IndustrialExplosiveEntity(EntityType<? extends IndustrialExplosiveEntity> entityType, Level world) {
        super(entityType, world);
    }

    public IndustrialExplosiveEntity(EntityType<? extends IndustrialExplosiveEntity> entityType, LivingEntity owner, Level world) {
        super(entityType, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.INDUSTRIAL_EXPLOSIVE.get();
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide()) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            float power = 10.0f;

            this.level().explode(this, x, y, z, power, Level.ExplosionInteraction.TNT);
            this.discard();
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.INDUSTRIAL_EXPLOSIVE.get());
    }
}
