package com.besson.endfield.blockEntity.custom.combat;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HeavyGunTowerBlockEntity extends BaseGunTower {
    public HeavyGunTowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HEAVY_GUN_TOWER.get(), pos, state);
    }

    @Override
    protected float getRange() {
        return 15.0f;
    }

    @Override
    protected int getFireInternal() {
        return 40;
    }

    @Override
    protected int getPowerCostPerTick() {
        return 20;
    }

    @Override
    protected void shoot(Level world) {
        if (targetUuid == null) return;
        Entity target = ((ServerLevel) world).getEntity(targetUuid);
        if (!(target instanceof LivingEntity t)) return;
        if (!t.isAlive()) return;

        Vec3 towerPos = Vec3.atCenterOf(getBlockPos()).add(0, 3, 0);
        Vec3 targetPos = t.position().add(0, t.getBbHeight(), 0);

        spawnBulletTrail(world, towerPos, targetPos);
        t.invulnerableTime = 0;
        boolean died = t.hurt(world.damageSources().generic(), 21.0f);
        world.playSound(null, this.getBlockPos(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f);
        if (died || t.isDeadOrDying()) {
            onKilled(t);
        }
        this.fireCooldown = FIRE_INTERNAL;
    }
}
