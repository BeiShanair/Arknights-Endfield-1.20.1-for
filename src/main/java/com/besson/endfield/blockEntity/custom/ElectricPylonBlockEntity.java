package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.block.ElectrifiableDevice;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ElectricPylonBlockEntity extends BlockEntity implements GeoBlockEntity {
    private BlockPos connectedNode;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ElectricPylonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRIC_PYLON.get(), pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ElectricPylonBlockEntity entity) {
        if (world.isClientSide()) return;

        if (entity.connectedNode == null || world.getBlockEntity(entity.connectedNode) == null) {
            BlockPos closest = null;
            double closestDist = Double.MAX_VALUE;

            for (BlockPos p: BlockPos.betweenClosed(pos.offset(-10, -10, -10), pos.offset(10, 10, 10))) {
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

        if (entity.connectedNode != null) {
            ProtocolAnchorCoreBlockEntity core = entity.findCore(world);
            if (core != null && core.canSupplyPower()) {
                entity.supplyPower(core);
            }
        }
    }

    private ProtocolAnchorCoreBlockEntity findCore(Level world) {
        if (connectedNode == null) return null;
        BlockEntity be = world.getBlockEntity(connectedNode);
        if (be instanceof ProtocolAnchorCoreBlockEntity core) {
            return core;
        } else if (be instanceof RelayTowerBlockEntity relay) {
            return relay.getConnectedCore(world);
        }
        return null;
    }

    private void supplyPower(ProtocolAnchorCoreBlockEntity core) {
        if (level == null) return;
        if (core.getStoredPower() < 20) return;

        BlockPos pos = this.getBlockPos();
        for (BlockPos target: BlockPos.betweenClosed(pos.offset(-10, 0, -10), pos.offset(10, 0, 10))) {
            BlockEntity be = null;
            if (level != null) {
                be = level.getBlockEntity(target);
            }
            if (be instanceof ElectrifiableDevice device) {
                if (device.needsPower()) {
                    int required = device.getRequiredPower();
                    device.receiveElectricCharge(required * 2);
                    core.consumePower(required);
                }
            }
        }
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
