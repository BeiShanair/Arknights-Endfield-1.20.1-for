package com.besson.endfield.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public record NodeEntry(BlockPos pos, NodeType type) {
    public CompoundTag writeNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putLong("pos", pos.asLong());
        nbt.putString("type", type.name());
        return nbt;
    }
    
    public static NodeEntry fromNbt(CompoundTag nbt) {
        BlockPos pos = BlockPos.of(nbt.getLong("pos"));
        NodeType type = NodeType.valueOf(nbt.getString("type"));
        return new NodeEntry(pos, type);
    }
}
