package com.besson.endfield.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

public class NodeState extends SavedData {
    public Set<NodeEntry> nodeEntries = new HashSet<>();
    
    public NodeState() {}

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (NodeEntry entry : nodeEntries) {
            list.add(entry.writeNbt());
        }
        nbt.put("nodes", list);
        return nbt;
    }
    
    public static NodeState fromNbt(CompoundTag nbt) {
        NodeState state = new NodeState();
        ListTag list = nbt.getList("nodes", 10);
        for (int i = 0; i < list.size(); i++) {
            state.nodeEntries.add(NodeEntry.fromNbt(list.getCompound(i)));
        }
        return state;
    }
}

