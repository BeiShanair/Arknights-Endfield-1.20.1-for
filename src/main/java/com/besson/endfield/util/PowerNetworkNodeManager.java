package com.besson.endfield.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class PowerNetworkNodeManager {
    private static final Map<ServerLevel, PowerNetworkNodeManager> INSTANCE = new WeakHashMap<>();
    private final ServerLevel world;
    private final NodeState state;
    
    public PowerNetworkNodeManager(ServerLevel world) {
        this.world = world;
        DimensionDataStorage manager = world.getDataStorage();

        String dimKey = world.dimension().registry().toString().replace(':', '_').replace('/', '_');
        String stateName = "power_network_state_node_" + dimKey;
        
        this.state = manager.computeIfAbsent(NodeState::fromNbt, NodeState::new, stateName);
    }
    
    public static PowerNetworkNodeManager get(ServerLevel world) {
        return INSTANCE.computeIfAbsent(world, PowerNetworkNodeManager::new);
    }

    public void register(NodeEntry entry) {
        if (state.nodeEntries.add(entry)) {
            state.setDirty();
        }
    }

    public void unregister(BlockPos pos) {
        if (state.nodeEntries.removeIf(e -> e.pos().equals(pos))) {
            state.setDirty();
        }
    }

    public Optional<NodeEntry> findNearest(BlockPos from, NodeType type, int range) {
        NodeEntry nearest = null;
        int best = Integer.MAX_VALUE;

        for (NodeEntry entry : state.nodeEntries) {
            if (entry.pos().equals(from)) continue;
            
            if (!type.canConnectTo(entry.type())) continue;

            double dist = Math.sqrt(entry.pos().distSqr(from));
            if (dist <= range && dist < best) {
                best = (int) dist;
                nearest = entry;
            }
        }
        return Optional.ofNullable(nearest);
    }

    public ServerLevel getWorld() {
        return world;
    }
}
