package com.besson.endfield.blockEntity.custom.combat;


import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HeGrenadeTowerBlockEntity extends BaseGunTower {
    public HeGrenadeTowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HE_GRENADE_TOWER.get(), pos, state);
    }

    @Override
    protected float getRange() {
        return 12.5f;
    }

    @Override
    protected int getFireInternal() {
        return 60;
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

        Vec3 towerPos = Vec3.atCenterOf(getBlockPos()).add(0, 2.5, 0);
        Vec3 targetPos = t.position().add(0, t.getBbHeight(), 0);

        spawnTrail(world, towerPos, targetPos);
        t.invulnerableTime = 0;
        boolean died = t.hurt(world.damageSources().generic(), 55.0f);
        world.explode(null, targetPos.x, targetPos.y, targetPos.z, 0.0f, Level.ExplosionInteraction.NONE);
        if (died || t.isDeadOrDying()) {
            onKilled(t);
        }
        AABB box = new AABB(targetPos, targetPos).inflate(3);
        for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && !(e instanceof Player))) {
            double distance = e.position().distanceTo(targetPos);
            if (distance <= 5) {
                double damage = 55.0 * (1 - distance / 5);
                e.invulnerableTime = 0;
                boolean died2 = e.hurt(world.damageSources().generic(), (float) damage);
                if (died2 || t.isDeadOrDying()) {
                    onKilled(t);
                }
            }
        }
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                double dist = x * x + z * z;

                if (dist > 5 * 5) continue;
                if (world.random.nextFloat() > 0.6f) continue;

                BlockPos firePos = t.blockPosition();
                BlockPos groundPos = firePos.below();

                if (!world.getBlockState(groundPos).isSolid()) {
                    continue;
                }
                if (!world.getBlockState(firePos).isAir()) {
                    continue;
                }
                world.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
            }
        }
        world.playSound(null, this.getBlockPos(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f);
        this.fireCooldown = FIRE_INTERNAL;
    }

    protected static void spawnTrail(Level level, Vec3 start, Vec3 end) {
        Vec3 direction = end.subtract(start);
        double length = direction.length();
        Vec3 step = direction.normalize().scale(0.5);
        int steps = (int)(length / 0.5);
        Vec3 pos = start;
        for (int i = 0; i < steps; i++) {
            ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
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
