package com.forpleuvoir.suika.mixin.server;

import com.forpleuvoir.suika.Suika;
import com.forpleuvoir.suika.server.data.WarpPoint;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelStorage;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.mixin.server
 * @class_name MinecraftServerMixin
 * @create_time 2020/11/23 10:18
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "startServer", at = @At("RETURN"))
    private static void startServer(Function<Thread, CallbackI.S> serverFactory, CallbackInfoReturnable<MinecraftServer> returnable) {
        Suika.LOGGER.info("suika mod server mixin...");
        try {
            Field field = MinecraftServer.class.getDeclaredField("session");
            field.setAccessible(true);
            LevelStorage.Session session=(LevelStorage.Session)field.get(returnable.getReturnValue());
            WarpPoint.initialize(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
