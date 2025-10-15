package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashSet;
import java.util.Set;

public class RelayTowerBlockEntity extends BlockEntity implements GeoBlockEntity {
    private BlockPos connectedNode;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RelayTowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RELAY_TOWER.get(), pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, RelayTowerBlockEntity entity) {
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

    public BlockPos getConnectedNode() {
        return connectedNode;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 11, 0);
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

    public ProtocolAnchorCoreBlockEntity getConnectedCore(Level world) {
        return findConnectedCore(world, new HashSet<>());
    }

    private ProtocolAnchorCoreBlockEntity findConnectedCore(Level world, Set<BlockPos> visited) {
        if (connectedNode == null) return null;
        if (visited.contains(getBlockPos())) return null;

        visited.add(getBlockPos());

        BlockEntity be = world.getBlockEntity(connectedNode);
        if (be instanceof ProtocolAnchorCoreBlockEntity core) {
            return core;
        }

        if (be instanceof RelayTowerBlockEntity relay) {
            return relay.findConnectedCore(world, visited);
        }
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
