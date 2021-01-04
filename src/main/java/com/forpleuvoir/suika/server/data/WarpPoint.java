package com.forpleuvoir.suika.server.data;

import com.forpleuvoir.chatbubbles.ReflectionUtils;
import com.forpleuvoir.suika.Suika;
import com.forpleuvoir.suikalib.util.FileUtil;
import com.forpleuvoir.suikalib.util.JsonUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.data
 * @class_name WarpPoint
 * @create_time 2020/11/22 14:08
 */

public class WarpPoint {
    public static final Map<String, Pos> warpPoints = Maps.newHashMap();
    public static final Map<String, Pos> homePoints = Maps.newHashMap();
    public static final Map<String, Pos> backPoints = Maps.newHashMap();
    private static File file;
    private static File filePath;

    public static void initialize(LevelStorage.Session session) {
        Constructor<WorldSavePath> constructor;
        try {
            constructor = WorldSavePath.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            WorldSavePath worldSavePath = constructor.newInstance("suika");
            assert session != null;
            filePath = session.getDirectory(worldSavePath).toFile();
            file = new File(filePath, "warp_point.json");
            load();
        } catch (Exception e) {
            Suika.LOGGER.warn("suika mod warp_point load failed...");
            if (!file.exists()) {
                try {
                    createFile();
                } catch (IOException ioException) {
                    Suika.LOGGER.warn("suika mod warp_point create failed...");
                    ioException.printStackTrace();
                }
            }
        }
    }

    private static void createFile() throws IOException {
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static void setBack(String uuid, Pos pos) {
        backPoints.put(uuid, pos);
        save();
    }

    public static void setBack(ServerPlayerEntity player) {
        backPoints.put(player.getUuidAsString(), new Pos(player.getPos(), getDimension(player)));
        save();
    }

    public static void addWarp(String key, ServerPlayerEntity player) {
        warpPoints.put(key, new Pos(player.getPos(), getDimension(player)));
        save();
    }

    public static void remove(String arg) {
        warpPoints.remove(arg);
        save();
    }

    public static void sethome(ServerPlayerEntity player) {
        homePoints.put(player.getUuidAsString(), new Pos(player.getPos(), getDimension(player)));
        save();
    }

    public static Dimension getDimension(DimensionType type) {
        WarpPoint.Dimension dimension = WarpPoint.Dimension.OVERWORLD;
        try {
            Field THE_NETHER = DimensionType.class.getDeclaredField("THE_NETHER");
            Field THE_END = DimensionType.class.getDeclaredField("THE_END");
            if (type.equals(THE_NETHER.get(DimensionType.class))) {
                dimension = WarpPoint.Dimension.NETHER;
            } else if (type.equals(THE_END.get(DimensionType.class))) {
                dimension = WarpPoint.Dimension.END;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dimension;
    }

    public static Dimension getDimension(ServerPlayerEntity player) {
        DimensionType type = player.getServerWorld().getDimension();
        WarpPoint.Dimension dimension = WarpPoint.Dimension.OVERWORLD;
        try {
            DimensionType THE_NETHER = (DimensionType) ReflectionUtils.getPrivateFieldValueByType(null, DimensionType.class, DimensionType.class, 1);
            DimensionType THE_END = (DimensionType) ReflectionUtils.getPrivateFieldValueByType(null, DimensionType.class, DimensionType.class, 2);
            if (type.equals(THE_NETHER)) {
                dimension = WarpPoint.Dimension.NETHER;
            } else if (type.equals(THE_END)) {
                dimension = WarpPoint.Dimension.END;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dimension;
    }

    private static void save() {
        JsonObject warps = JsonUtil.gson.toJsonTree(warpPoints).getAsJsonObject();
        JsonObject homes = JsonUtil.gson.toJsonTree(homePoints).getAsJsonObject();
        JsonObject backs = JsonUtil.gson.toJsonTree(backPoints).getAsJsonObject();
        JsonObject json = new JsonObject();
        json.add("warps", warps);
        json.add("homes", homes);
        json.add("backs", backs);
        try {
            FileUtil.writeFile(file, JsonUtil.gson.toJson(json), false);
        } catch (IOException e) {
            Suika.LOGGER.warn("suika mod warp_point file write failed...");
        }
    }

    private static void load() throws FileNotFoundException {
        JsonObject json = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
        Map<String, Pos> homes = JsonUtil.fromJson(json.get("homes"), new TypeToken<Map<String, Pos>>() {
        }.getType());
        Map<String, Pos> warps = JsonUtil.fromJson(json.get("warps"), new TypeToken<Map<String, Pos>>() {
        }.getType());
        Map<String, Pos> backs = JsonUtil.fromJson(json.get("backs"), new TypeToken<Map<String, Pos>>() {
        }.getType());
        warpPoints.putAll(warps);
        homePoints.putAll(homes);
        backPoints.putAll(backs);

    }


    public static void back(ServerPlayerEntity player) {
        String uuid = player.getUuidAsString();
        if (backPoints.isEmpty())
            return;
        if (backPoints.containsKey(uuid)) {
            Pos pos = backPoints.get(uuid);
            ServerWorld serverWorld;
            switch (pos.dimension) {
                case END:
                    serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.END);
                    break;
                case NETHER:
                    serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);
                    break;
                case OVERWORLD:
                default:
                    serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
            }
            Vec3d vec3d = pos.position;
            teleport(player, serverWorld, vec3d.getX(), vec3d.getY(), vec3d.getZ(), player.yaw, player.pitch);
        }
    }

    public static boolean home(ServerPlayerEntity player) {
        String uuid = player.getUuidAsString();
        if (homePoints.isEmpty())
            return false;
        if (homePoints.containsKey(uuid)) {
            Pos pos = homePoints.get(uuid);
            ServerWorld serverWorld;
            switch (pos.dimension) {
                case END:
                    serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.END);
                    break;
                case NETHER:
                    serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);
                    break;
                case OVERWORLD:
                default:
                    serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
            }
            Vec3d vec3d = pos.position;
            teleport(player, serverWorld, vec3d.getX(), vec3d.getY(), vec3d.getZ(), player.yaw, player.pitch);
            return true;
        }
        return false;
    }


    public static boolean warp(ServerPlayerEntity player, String key) {
        if (warpPoints.isEmpty()) {
            return false;
        }
        if (!warpPoints.containsKey(key))
            return false;
        Pos pos = warpPoints.get(key);
        ServerWorld serverWorld;
        switch (pos.dimension) {
            case END:
                serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.END);
                break;
            case NETHER:
                serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.NETHER);
                break;
            case OVERWORLD:
            default:
                serverWorld = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
        }
        Vec3d vec3d = pos.position;
        teleport(player, serverWorld, vec3d.getX(), vec3d.getY(), vec3d.getZ(), player.yaw, player.pitch);
        return true;
    }


    public static class Pos {
        public Vec3d position;
        public Dimension dimension;

        public Pos(Vec3d position, Dimension dimension) {
            this.position = position;
            this.dimension = dimension;
        }
    }

    public enum Dimension {
        OVERWORLD, END, NETHER
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch) {
        player.setCameraEntity(player);
        player.stopRiding();
        setBack(player);
        if (targetWorld == player.world) {
            player.networkHandler.requestTeleport(x, y, z, yaw, pitch);
        } else {
            ServerWorld serverWorld = player.getServerWorld();
            WorldProperties worldProperties = targetWorld.getLevelProperties();
            player.networkHandler.sendPacket(new PlayerRespawnS2CPacket(targetWorld.getDimension(), targetWorld.getRegistryKey(), BiomeAccess.hashSeed(targetWorld.getSeed()), player.interactionManager.getGameMode(), player.interactionManager.getPreviousGameMode(), targetWorld.isDebugWorld(), targetWorld.isFlat(), true));
            player.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
            player.server.getPlayerManager().sendCommandTree(player);
            serverWorld.removePlayer(player, Entity.RemovalReason.CHANGED_DIMENSION);
            //player.unsetRemoved();
            ReflectionUtils.setPrivateFieldValueByType(player,Entity.RemovalReason.class,null,0);
            player.refreshPositionAndAngles(x, y, z, yaw, pitch);
            player.setWorld(targetWorld);
            targetWorld.onPlayerTeleport(player);
//            player.worldChanged(serverWorld);
            player.networkHandler.requestTeleport(x, y, z, yaw, pitch);
            player.interactionManager.setWorld(targetWorld);
            player.server.getPlayerManager().sendWorldInfo(player, targetWorld);
            player.server.getPlayerManager().sendPlayerStatus(player);
        }

    }
}
