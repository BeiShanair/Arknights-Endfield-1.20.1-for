package com.besson.endfield.blockEntity.custom.combat;

import com.besson.endfield.blockEntity.custom.powering.ElectricPylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.RelayTowerBlockEntity;
import com.besson.endfield.util.NodeType;
import com.besson.endfield.util.PowerNetworkManager;
import com.besson.endfield.util.PowerNetworkNodeManager;
import com.besson.endfield.util.TurretFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BaseGunTower extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected final double RANGE = getRange();
    protected final int FIRE_INTERNAL = getFireInternal();

    protected static final float MAX_YAW_SPEED = 6.0f;
    protected static final float MAX_PITCH_SPEED = 4.0f;

    protected float turretYaw;
    protected float turretPitch;

    protected int fireCooldown = 0;
    protected UUID targetUuid = null;
    protected static final float AIM_ANGLE_TOLERANCE = 3.0f;
    protected float targetDesiredYaw;
    protected float targetDesiredPitch;

    protected int tickNum = 0;
    protected boolean isPowered = false;
    protected boolean registeredToManager = false;
    protected int storedPower;
    protected static final int MAX_STORED_POWER = 10000;
    protected boolean isWorking;
    protected boolean enable = true;
    protected boolean needsInit = true;
    
    public BaseGunTower(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected abstract float getRange();
    protected abstract int getFireInternal();
    protected abstract int getPowerCostPerTick();

    public static <T extends BaseGunTower> void tick(Level world, BlockPos pos, BlockState state, T be) {
        if (world.isClientSide()) return;

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
        
        if (!be.isPowered && be.storedPower < be.getPowerCostPerTick()) return;
        
        be.updateTarget(world);
        be.updateRotation(world);

        if (be.fireCooldown > 0) {
            be.fireCooldown--;
        } else if (be.hasTarget()) {
            if (be.isAimed()) {
                be.shoot(world);
                be.storedPower -= be.getPowerCostPerTick();
            }
        }

        be.setChanged();
    }

    public boolean getEnable() {
        return enable;
    }

    public void receiveElectricCharge(int amount) {
        this.storedPower = Math.min(this.storedPower + amount * 20, MAX_STORED_POWER);
    }
    
    public int getRequiredPower() {
        if (isWorking || isPowered && storedPower < MAX_STORED_POWER) {
            return getPowerCostPerTick();
        }
        return 0;
    }
    
    protected void updateTarget(Level world) {
        if (targetUuid != null) {
            Entity e = ((ServerLevel) world).getEntity(targetUuid);
            if (e instanceof Monster && e.isAlive() && e.distanceToSqr(Vec3.atCenterOf(getBlockPos())) < RANGE * RANGE) {
                return;
            }
            targetUuid = null;
        }
        
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class,
                        new AABB(getBlockPos()).inflate(RANGE), LivingEntity::isAlive);
        list.removeIf(e -> !(e instanceof Monster));
        if (!list.isEmpty()) {
            targetUuid = list.get(0).getUUID();
        }
    }
    
    protected boolean hasTarget() {
        return targetUuid != null;
    }
    
    protected void updateRotation(Level world) {
        if (targetUuid == null) return;
        Entity target = ((ServerLevel) world).getEntity(targetUuid);
        if (target == null) return;

        Vec3 towerPos = Vec3.atCenterOf(getBlockPos()).add(0, 1.5, 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
        Vec3 direction = targetPos.subtract(towerPos).normalize();

        // 计算目标角度（度），对 yaw 加 180° 修正以匹配模型朝向
        float desiredYaw = (float) (Mth.atan2(direction.x, direction.z) * (180f / Math.PI)) + 180f;
        float desiredPitch = (float) -(Math.asin(direction.y) * (180f / Math.PI));

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

    protected boolean isAimed() {
        if (targetUuid == null) return false;
        float yawDiff = Math.abs(wrapDegrees(this.turretYaw - this.targetDesiredYaw));
        float pitchDiff = Math.abs(wrapDegrees(this.turretPitch - this.targetDesiredPitch));
        return yawDiff <= AIM_ANGLE_TOLERANCE && pitchDiff <= AIM_ANGLE_TOLERANCE;
    }

    protected float approachAngle(float current, float target, float maxDelta) {
        float delta = wrapDegrees(target - current);
        if (delta > maxDelta) delta = maxDelta;
        if (delta < -maxDelta) delta = -maxDelta;
        return current + delta;
    }

    protected float wrapDegrees(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) angle -= 360.0f;
        if (angle < -180.0f) angle += 360.0f;
        return angle;
    }

    protected abstract void shoot(Level world);

    protected void onKilled(LivingEntity target) {
        if (!(level instanceof ServerLevel serverWorld)) return;

        int exp = getExperienceReward(target);

        if (exp > 0) {
            ExperienceOrb.award(serverWorld, target.position(), exp);
        }
    }

    protected int getExperienceReward(LivingEntity target) {
        return target.getExperienceReward();
    }
    
    protected static void spawnBulletTrail(Level level, Vec3 start, Vec3 end) {

        Vec3 direction = end.subtract(start);
        double length = direction.length();
        Vec3 step = direction.normalize().scale(0.5);
        int steps = (int)(length / 0.5);
        Vec3 pos = start;
        for (int i = 0; i < steps; i++) {
            ((ServerLevel) level).sendParticles(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            pos = pos.add(step);
        }
    }

    public float getTurretPitch() {
        return turretPitch;
    }

    public float getTurretYaw() {
        return turretYaw;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("fireCooldown", fireCooldown);
        if (targetUuid != null) {
            nbt.putUUID("targetUuid", targetUuid);
        }
        nbt.putFloat("turretYaw", turretYaw);
        nbt.putFloat("turretPitch", turretPitch);
        nbt.putInt("storedPower", this.storedPower);
        nbt.putBoolean("isWorking", this.isWorking);
        nbt.putBoolean("enable", this.enable);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.fireCooldown = nbt.getInt("fireCooldown");
        if (nbt.contains("targetUuid")) {
            this.targetUuid = nbt.getUUID("targetUuid");
        }
        this.turretYaw = nbt.getFloat("turretYaw");
        this.turretPitch = nbt.getFloat("turretPitch");
        this.storedPower = nbt.getInt("storedPower");
        this.isWorking = nbt.getBoolean("isWorking");
        this.enable = nbt.getBoolean("enable");
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
