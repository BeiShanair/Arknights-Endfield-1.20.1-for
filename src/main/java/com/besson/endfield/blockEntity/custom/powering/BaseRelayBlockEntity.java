package com.besson.endfield.blockEntity.custom.powering;

import com.besson.endfield.util.power.NodeEntry;
import com.besson.endfield.util.power.NodeType;
import com.besson.endfield.util.power.PowerNetworkNodeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public abstract class BaseRelayBlockEntity extends BlockEntity {
    protected @Nullable BlockPos connectedNode;
    protected boolean needsInit = true;
    protected boolean isPowered = false;
    protected int tickNum = 0;

    protected BaseRelayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static <T extends BaseRelayBlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        if (level.isClientSide()) return;

        if (be.needsInit && level instanceof ServerLevel serverLevel) {
            be.needsInit = false;

            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverLevel);
            manager.register(new NodeEntry(pos, NodeType.RELAY));

            if (be.connectedNode == null) {
                manager.findNearest(pos, NodeType.RELAY, 80).ifPresent(target -> {
                    be.connectedNode = target.pos();
                    be.isPowered = true;
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                });
            }
        }

        be.tickNum++;
        if (be.tickNum % 20 == 0) {
            be.tickNum = 0;
            if (be.connectedNode == null) return;

            if (level.getBlockEntity(be.connectedNode) == null) {
                be.removeConnectedNode();
                if (level instanceof ServerLevel serverLevel) {
                    PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverLevel);
                    manager.findNearest(pos, NodeType.RELAY, 80).ifPresent(target -> {
                        be.connectedNode = target.pos();
                        be.isPowered = true;
                        be.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                    });
                } else {
                    be.isPowered = false;
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level instanceof ServerLevel) {
            needsInit = true;
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel serverLevel) {
            PowerNetworkNodeManager.get(serverLevel).unregister(this.getBlockPos());
        }
        super.setRemoved();
    }

    public @Nullable BlockPos getConnectedNode() {
        return connectedNode;
    }

    public void setConnectedNode(@Nullable BlockPos recorded) {
        this.connectedNode = recorded;
    }

    public void removeConnectedNode() {
        this.connectedNode = null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (connectedNode != null) {
            tag.putLong("connected", connectedNode.asLong());
        }
        tag.putBoolean("isPowered", isPowered);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("connected")) {
            connectedNode = BlockPos.of(tag.getLong("connected"));
        } else if (tag.contains("connectedNode")) {
            connectedNode = BlockPos.of(tag.getLong("connectedNode"));
        }
        isPowered = tag.getBoolean("isPowered");
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
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(0, 11, 0);
    }
}
