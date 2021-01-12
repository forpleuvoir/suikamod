package com.forpleuvoir.suika.mixin.command.argument;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.mixin.command.argument
 * @class_name ArgumentTypesMixin
 * @create_time 2021/1/7 21:10
 */

import com.forpleuvoir.suika.server.command.arguments.WarpPointArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.forpleuvoir.suika.Suika.LOGGER;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {


    @Shadow
    public static <T extends ArgumentType<?>> void register(String id, Class<T> class_, ArgumentSerializer<T> argumentSerializer) {
    }

    @Inject(method = "register()V",cancellable = true,at = @At("RETURN"))
    private static void register(CallbackInfo ci){
        LOGGER.info("suika mod ArgumentTypes mixin...");
        register("warps", WarpPointArgumentType.class,new WarpPointArgumentType.Serializer());
    }
}
