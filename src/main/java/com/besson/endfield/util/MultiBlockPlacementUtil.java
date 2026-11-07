package com.besson.endfield.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class MultiBlockPlacementUtil {

    private MultiBlockPlacementUtil() {
    }
    
    public static boolean isReplaceable(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return true;
        }
        if (state.canBeReplaced()) {
            return true;
        }
        
        return !state.hasBlockEntity() && state.getCollisionShape(level, pos).isEmpty();
    }
    
    public static boolean isAllReplaceable(LevelReader level, BlockPos[] positions) {
        for (BlockPos pos : positions) {
            if (!isReplaceable(level, pos)) {
                return false;
            }
        }
        return true;
    }
    
    public static BlockPos[] betweenClosed(BlockPos from, BlockPos to) {
        return BlockPos.betweenClosedStream(from, to).map(BlockPos::immutable).toArray(BlockPos[]::new);
    }
    
    public static void pushEntitiesOut(ServerLevel level, BlockPos mainPos, BlockPos[] occupied) {
        AABB structure = structureBox(mainPos, occupied);
        List<AABB> cubes = collectCubes(mainPos, occupied);

        for (Entity entity : level.getEntities((Entity) null, structure, e -> e.blocksBuilding && e.isAlive())) {
            Vec3 center = entity.getBoundingBox().getCenter();
            if (cubes.stream().noneMatch(cube -> cube.contains(center))) {
                continue;
            }
            pushOut(level, entity, structure);
        }
    }

    private static AABB structureBox(BlockPos mainPos, BlockPos[] occupied) {
        int minX = mainPos.getX();
        int minY = mainPos.getY();
        int minZ = mainPos.getZ();
        int maxX = minX;
        int maxY = minY;
        int maxZ = minZ;
        for (BlockPos pos : occupied) {
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
        }
        return new AABB(minX, minY, minZ, maxX + 1.0D, maxY + 1.0D, maxZ + 1.0D);
    }

    private static List<AABB> collectCubes(BlockPos mainPos, BlockPos[] occupied) {
        List<AABB> cubes = new ArrayList<>(occupied.length + 1);
        cubes.add(cube(mainPos));
        for (BlockPos pos : occupied) {
            cubes.add(cube(pos));
        }
        return cubes;
    }

    private static AABB cube(BlockPos pos) {
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D);
    }

    private static void pushOut(ServerLevel level, Entity entity, AABB structure) {
        AABB entityBox = entity.getBoundingBox();
        double halfWidth = entity.getBbWidth() / 2.0D + 1.0E-3D;
        double[][] candidates = {
                {entity.getX(), structure.maxY + 1.0E-3D, entity.getZ()},
                {entity.getX(), entity.getY(), structure.minZ - halfWidth},
                {entity.getX(), entity.getY(), structure.maxZ + halfWidth},
                {structure.minX - halfWidth, entity.getY(), entity.getZ()},
                {structure.maxX + halfWidth, entity.getY(), entity.getZ()}
        };

        double[] best = null;
        double bestDistance = Double.MAX_VALUE;
        for (double[] candidate : candidates) {
            double dx = candidate[0] - entity.getX();
            double dy = candidate[1] - entity.getY();
            double dz = candidate[2] - entity.getZ();
            double distance = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
            if (distance >= bestDistance) {
                continue;
            }
            if (!level.noCollision(entity, entityBox.move(dx, dy, dz))) {
                continue;
            }
            best = candidate;
            bestDistance = distance;
        }

        if (best != null) {
            entity.teleportTo(best[0], best[1], best[2]);
            entity.resetFallDistance();
        }
    }
}
