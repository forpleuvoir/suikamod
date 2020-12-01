package com.forpleuvoir.suika.server.command;

import com.forpleuvoir.suika.server.data.WarpPoint;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.command
 * @class_name HomeCommand
 * @create_time 2020/11/22 13:50
 */

public class HomeCommand {
    private static final SimpleCommandExceptionType homeException=new SimpleCommandExceptionType(new TranslatableText("command.suika.exception.home"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("home").executes(HomeCommand::home));
        dispatcher.register(CommandManager.literal("sethome").executes(HomeCommand::setHome));
    }

    public static int home(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(!WarpPoint.home(player)){
           throw homeException.create();
        }
        return 1;
    }

    public static int setHome(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        WarpPoint.sethome(player);
        return 1;
    }

}
