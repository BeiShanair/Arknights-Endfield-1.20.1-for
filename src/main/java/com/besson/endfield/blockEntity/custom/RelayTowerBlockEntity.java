package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.util.NodeEntry;
import com.besson.endfield.util.NodeType;
import com.besson.endfield.util.PowerNetworkNodeManager;
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
import software.bernie.geckolib.util.GeckoLibUtil;

public class RelayTowerBlockEntity extends BlockEntity implements GeoBlockEntity {
    private BlockPos connectedNode;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean needsInit = true;
    private boolean isPowered = false;
    protected int tickNum = 0;
    
    public RelayTowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RELAY_TOWER.get(), pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, RelayTowerBlockEntity be) {
        if (world.isClientSide()) return;

        if (be.needsInit && world instanceof ServerLevel serverWorld) {
            be.needsInit = false;

            PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverWorld);
            manager.register(new NodeEntry(pos, NodeType.RELAY));

            if (be.connectedNode == null) {
                manager.findNearest(pos, NodeType.RELAY, 80).ifPresent(target -> {
                    be.connectedNode = target.pos();
                    be.isPowered = true;
                    be.setChanged();
                    world.sendBlockUpdated(pos, state, state, 3);
                });
            }
        }

        if (be.tickNum % 20 == 0) {
            be.tickNum = 0;
            if (be.connectedNode == null) return;

            if (world.getBlockEntity(be.connectedNode) == null) {
                be.removeConnectedNode();
                if (world instanceof ServerLevel serverWorld) {
                    PowerNetworkNodeManager manager = PowerNetworkNodeManager.get(serverWorld);

                    manager.findNearest(pos, NodeType.RELAY, 80).ifPresent(target -> {
                        be.connectedNode = target.pos();
                        be.isPowered = true;
                        be.setChanged();
                        world.sendBlockUpdated(pos, state, state, 3);
                    });
                } else {
                    be.isPowered = false;
                    be.setChanged();
                    world.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        if (pLevel instanceof ServerLevel) {
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
            pTag.putLong("connected", connectedNode.asLong());
        }
        pTag.putBoolean("isPowered", isPowered);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("connected")) {
            connectedNode = BlockPos.of(pTag.getLong("connected"));
        }
        isPowered = pTag.getBoolean("isPowered");
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

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
    public void setConnectedNode(BlockPos recorded) {
        this.connectedNode = recorded;
    }

    public void removeConnectedNode() {
        this.connectedNode = null;
    }
    
}
