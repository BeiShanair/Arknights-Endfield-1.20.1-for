package com.besson.endfield.blockEntity.custom.combat;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LNTowerBlockEntity extends BaseGunTower {
    public LNTowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LN_TOWER.get(), pos, state);
    }

    @Override
    protected float getRange() {
        return 15f;
    }

    @Override
    protected int getFireInternal() {
        return 100;
    }

    @Override
    protected int getPowerCostPerTick() {
        return 10;
    }

    @Override
    protected void shoot(Level world) {
        if (targetUuid == null) return;
        Entity target = ((ServerLevel) world).getEntity(targetUuid);
        if (target == null) return;

        Vec3 towerPos = Vec3.atCenterOf(getBlockPos()).add(0, 2.5, 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight(), 0);

        spawnBulletTrail(world, towerPos, targetPos);
        AABB box = new AABB(targetPos, targetPos).inflate(3);
        for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && !(e instanceof Player))) {
            e.forceAddEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100), null);
        }
        world.playSound(null, this.getBlockPos(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f);

        this.fireCooldown = FIRE_INTERNAL;
    }

    protected static void spawnBulletTrail(Level level, Vec3 start, Vec3 end) {
        Vec3 direction = end.subtract(start);
        double length = direction.length();
        Vec3 step = direction.normalize().scale(0.5);
        int steps = (int)(length / 0.5);
        Vec3 pos = start;
        for (int i = 0; i < steps; i++) {
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            pos = pos.add(step);
        }
    }

    protected void updateRotation(Level world) {
        if (targetUuid == null) return;
        Entity target = ((ServerLevel) world).getEntity(targetUuid);
        if (target == null) return;

        Vec3 towerPos = Vec3.atCenterOf(getBlockPos()).add(0, 1.5, 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
        Vec3 direction = targetPos.subtract(towerPos).normalize();

        float desiredYaw = (float) (Mth.atan2(direction.x, direction.z) * (180f / Math.PI)) + 180f;
        float desiredPitch = (float) (Math.asin(direction.y) * (180f / Math.PI));

        float prevYaw = this.turretYaw;
        float prevPitch = this.turretPitch;

        this.targetDesiredYaw = desiredYaw;
        this.targetDesiredPitch = desiredPitch;

        this.turretYaw = approachAngle(this.turretYaw, desiredYaw, MAX_YAW_SPEED);
        this.turretPitch = approachAngle(this.turretPitch, desiredPitch, MAX_PITCH_SPEED);

        if (Math.abs(prevYaw - this.turretYaw) > 0.01f || Math.abs(prevPitch - this.turretPitch) > 0.01f) {
            world.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
}
