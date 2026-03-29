package com.besson.endfield.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TurretFakePlayer {
    private static final GameProfile PROFILE = 
            new GameProfile(UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"), "[Turret]");
    private static final Map<ServerLevel, ServerPlayer> CACHE = new HashMap<>();
    
    public static ServerPlayer get(ServerLevel world) {
        return CACHE.computeIfAbsent(world, TurretFakePlayer::createFakePlayer);
    }
    
    private static ServerPlayer createFakePlayer(ServerLevel world) {
        MinecraftServer server = world.getServer();
        ServerPlayer fakePlayer = new ServerPlayer(
                server, world, PROFILE);
        
        fakePlayer.setSilent(true);
        fakePlayer.setInvisible(true);
        
        return fakePlayer;
    }
}
