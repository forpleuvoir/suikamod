package com.forpleuvoir.suika.server.data;

import com.google.common.collect.Maps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Objects;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.data
 * @class_name Tpa
 * @create_time 2020/11/30 11:24
 */

public class Tpa {
    //String是目标玩家的uuid
    public static Map<String, Tpa> tpas = null;
    private static final Long time = 20 * 120L;

    public static void initialize() {
        tpas = Maps.newHashMap();
    }

    private final ServerPlayerEntity player;
    private final Vec3d target;
    private final WarpPoint.Dimension targetDimension;
    private final Long expireTime;

    public Tpa(ServerPlayerEntity player, ServerPlayerEntity target, Long nowTime) {
        this.player = player;
        this.target = target.getPos();
        this.targetDimension = WarpPoint.getDimension(target);
        this.expireTime = nowTime + time;
    }


    public boolean tpa(long nowTime) {
        if (canTp(nowTime)) {
            ServerWorld serverWorld;
            switch (targetDimension) {
                case END:
                    serverWorld = Objects.requireNonNull(this.player.getServer()).getWorld(World.END);
                    break;
                case NETHER:
                    serverWorld = Objects.requireNonNull(this.player.getServer()).getWorld(World.NETHER);
                    break;
                case OVERWORLD:
                default:
                    serverWorld = Objects.requireNonNull(this.player.getServer()).getWorld(World.OVERWORLD);
            }
            teleport(this.player, serverWorld, target);
            return true;
        } else {
            return false;
        }
    }

    private boolean canTp(long nowTime) {
        return this.expireTime > nowTime;
    }


    public ServerPlayerEntity getSender() {
        return player;
    }


    public Long getExpireTime() {
        return expireTime;
    }

    private static void teleport(ServerPlayerEntity player, ServerWorld serverWorld, Vec3d pos) {
        WarpPoint.teleport(player, serverWorld, pos.getX(), pos.getY(), pos.getZ(), player.yaw, player.pitch);
    }
}
