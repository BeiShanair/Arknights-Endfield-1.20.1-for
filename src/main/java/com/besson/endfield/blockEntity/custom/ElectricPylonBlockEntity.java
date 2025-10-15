package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.ElectrifiableDevice;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.power.PowerNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class ElectricPylonBlockEntity extends BlockEntity implements GeoBlockEntity {
    private BlockPos connectedNode;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean registeredToManager = false;

    public ElectricPylonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRIC_PYLON.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 8, 0);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ElectricPylonBlockEntity entity) {
        if (world.isClientSide()) return;

        if (entity.connectedNode == null || world.getBlockEntity(entity.connectedNode) == null) {
            BlockPos closest = null;
            double closestDist = Double.MAX_VALUE;

            for (BlockPos p: BlockPos.betweenClosed(pos.offset(-30, -30, -30), pos.offset(30, 30, 30))) {
                if (p.equals(pos)) continue;

                BlockEntity candidate = world.getBlockEntity(p);

                if (candidate instanceof ProtocolAnchorCoreBlockEntity || candidate instanceof RelayTowerBlockEntity) {
                    double d = pos.distSqr(p);
                    if (d < closestDist) {
                        closest = p.immutable();
                        closestDist = d;
                    }
                }
            }
            entity.connectedNode = closest;
            setChanged(world, pos, state);
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        if (!registeredToManager && pLevel instanceof ServerLevel serverLevel) {
            PowerNetworkManager.get(serverLevel).registerConsumer(this.getBlockPos(), () -> {
                try {
                    return this.getSurroundingDemand();
                } catch (Throwable t) {
                    return 0;
                }
            }, (amount) -> {
                try {
                    this.distributeToSurroundings(amount);
                } catch (Throwable ignored) {
                }
            });
            registeredToManager = true;
        }
    }

    private void distributeToSurroundings(Integer amount) {
        if (level == null || amount <= 0) return;
        List<ElectrifiableDevice> devices = new ArrayList<>();
        for (BlockPos target: BlockPos.betweenClosed(getBlockPos().offset(-10, -10, -10), getBlockPos().offset(10, 10, 10))) {
            BlockEntity be = level.getBlockEntity(target);
            if (be instanceof ElectrifiableDevice device) {
                if (device.needsPower()) {
                    devices.add(device);
                }
            }
        }
        if (devices.isEmpty()) return;
        int perDevice = amount / devices.size();
        for (ElectrifiableDevice device: devices) {
            int required = device.getRequiredPower();
            int toGive = Math.min(perDevice, required);
            device.receiveElectricCharge(toGive);
            amount -= toGive;
            if (amount <= 0) break;
        }
    }

    private Integer getSurroundingDemand() {
        if (level == null) return 0;
        int totalDemand = 0;
        for (BlockPos target: BlockPos.betweenClosed(getBlockPos().offset(-10, -10, -10), getBlockPos().offset(10, 10, 10))) {
            BlockEntity be = level.getBlockEntity(target);
            if (be instanceof ElectrifiableDevice device) {
                if (device.needsPower()) {
                    totalDemand += device.getRequiredPower();
                }
            }
        }
        return totalDemand;
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            PowerNetworkManager.get(serverLevel).unregisterConsumer(this.getBlockPos());
        }
        super.setRemoved();
    }

    public BlockPos getConnectedNode() {
        return connectedNode;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (connectedNode != null) {
            pTag.putLong("connectedNode", connectedNode.asLong());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("connectedNode")) {
            connectedNode = BlockPos.of(pTag.getLong("connectedNode"));
        }
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0,
                state -> state.setAndContinue(RawAnimation.begin().thenLoop("working"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
