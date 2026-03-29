package com.besson.endfield.blockEntity.custom.combat;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OmnidirectionalSonicTowerBlockEntity extends BaseGunTower {
    public OmnidirectionalSonicTowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OMNIDIRECTIONAL_SONIC_TOWER.get(), pos, state);
    }

    @Override
    protected float getRange() {
        return 6f;
    }

    @Override
    protected int getFireInternal() {
        return 100;
    }

    @Override
    protected int getPowerCostPerTick() {
        return 20;
    }

    @Override
    protected void shoot(Level world) {
        spawnCycleTrail(world, getBlockPos().getCenter());
        AABB box = new AABB(getBlockPos(), getBlockPos()).inflate(getRange());
        for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && !(e instanceof Player))) {
            e.forceAddEffect(new MobEffectInstance(MobEffects.LEVITATION, 60), null);
            e.forceAddEffect(new MobEffectInstance(MobEffects.CONFUSION, 100), null);
        }

        world.playSound(null, this.getBlockPos(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f);

        this.fireCooldown = FIRE_INTERNAL;
    }

    protected static void spawnCycleTrail(Level level, Vec3 start) {
        int steps = 120;

        for (int i = 0; i < steps; i++) {
            double angle = (double) i / steps * 2 * Math.PI;
            
            double vx = Math.cos(angle) * 4;
            double vz = Math.sin(angle) * 4;
            
            double speed = 0.2;

            ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD, start.x, start.y, start.z, 0, vx, 0, vz, speed);
        }
    }
}
