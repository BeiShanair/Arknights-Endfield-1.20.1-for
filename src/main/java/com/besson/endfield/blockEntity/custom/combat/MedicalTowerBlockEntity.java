package com.besson.endfield.blockEntity.custom.combat;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.RelayTowerBlockEntity;
import com.besson.endfield.util.power.NodeType;
import com.besson.endfield.util.power.PowerNetworkManager;
import com.besson.endfield.util.power.PowerNetworkNodeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MedicalTowerBlockEntity extends BaseGunTower {
    public MedicalTowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MEDICAL_TOWER.get(), pos, state);
    }

    @Override
    protected float getRange() {
        return 6;
    }

    @Override
    protected int getFireInternal() {
        return 100;
    }

    @Override
    protected int getPowerCostPerTick() {
        return 5;
    }

    @Override
    protected void shoot(Level world) {
        if (targetUuid == null) return;
        Entity target = ((ServerLevel) world).getEntity(targetUuid);
        if (target == null) return;
        if (target instanceof Player player) {
            Vec3 targetPos = player.position().add(0, target.getBbHeight() * 0.8, 0);

            player.heal(5.0f);
            ((ServerLevel) world).sendParticles(ParticleTypes.HEART, targetPos.x, targetPos.y, targetPos.z, 5, 0.5, 0.5, 0.5, 0);

            this.fireCooldown = FIRE_INTERNAL;
        }
    }

    public static void tick(Level world, BlockPos pos, BlockState state, MedicalTowerBlockEntity be) {
        if (world.isClientSide) return;

        if (be.needsInit && world instanceof ServerLevel serverWorld) {
            be.needsInit = false;
            PowerNetworkManager.get(serverWorld).registerConsumer(be.getBlockPos(), be::getRequiredPower, be::receiveElectricCharge);
            be.registeredToManager = true;
        }

        if (!be.getEnable()) {
            be.isWorking = false;
            world.sendBlockUpdated(pos, state, state, 3);
            be.setChanged();
            return;
        }
        be.tickNum++;

        if (be.tickNum % 20 == 0 && world instanceof ServerLevel serverWorld) {
            AtomicReference<BlockPos> t = new AtomicReference<>();
            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverWorld);
            manager.findNearest(pos, NodeType.CONSUMER, 10).ifPresent(target -> t.set(target.pos()));
            if (t.get() != null) {
                BlockEntity b = world.getBlockEntity(t.get());
                if (b instanceof ElectricPylonBlockEntity || b instanceof RelayTowerBlockEntity) {
                    be.isPowered = true;
                } else {
                    be.isPowered = false;
                    be.isWorking = false;
                    be.setChanged();
                    world.sendBlockUpdated(pos, state, state, 3);
                }
            }
            be.tickNum = 0;
        }

        if (!be.isPowered && be.storedPower < be.getRequiredPower()) return;

        be.updateTarget(world);

        if (be.fireCooldown > 0) {
            be.fireCooldown--;
        } else if (be.hasTarget()) {
            be.shoot(world);
            be.storedPower -= be.getPowerCostPerTick();

        }

        be.setChanged();
    }

    @Override
    protected void updateTarget(Level world) {
        if (targetUuid != null) {
            Entity e = ((ServerLevel) world).getEntity(targetUuid);
            if (e instanceof Player && e.isAlive() && e.distanceToSqr(Vec3.atCenterOf(getBlockPos())) < RANGE * RANGE) {
                return;
            }
            targetUuid = null;
        }

        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class,
                new AABB(getBlockPos()).inflate(RANGE), LivingEntity::isAlive);
        list.removeIf(e -> !(e instanceof Player));
        if (!list.isEmpty()) {
            targetUuid = list.get(0).getUUID();
        }
    }
}
