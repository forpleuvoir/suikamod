package com.forpleuvoir.suika.server.command;

import com.forpleuvoir.suika.server.command.arguments.WarpPointArgumentType;
import com.forpleuvoir.suika.server.data.WarpPoint;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.command
 * @class_name WarpCommand
 * @create_time 2020/11/23 11:59
 */

public class WarpCommand {
    private static final SimpleCommandExceptionType warpException = new SimpleCommandExceptionType(new TranslatableText("command.suika.exception.warp"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("warp")
                .then(CommandManager.argument("warp", new WarpPointArgumentType())
                        .executes(WarpCommand::warp))
        );
        dispatcher.register(CommandManager.literal("warps").executes(WarpCommand::warps));
        dispatcher.register(CommandManager.literal("setwarp").then(CommandManager.argument("warp", StringArgumentType.string()).executes(WarpCommand::setWarp)).requires(serverCommandSource-> serverCommandSource.hasPermissionLevel(2)));
        dispatcher.register(CommandManager.literal("removewarp").then(CommandManager.argument("warp",  new WarpPointArgumentType()).executes(WarpCommand::removeWarp)).requires(serverCommandSource-> serverCommandSource.hasPermissionLevel(2)));
    }


    private static int warp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        String arg = WarpPointArgumentType.getWarp(context, "warp");
        if (WarpPoint.warp(player, arg))
            source.sendFeedback(new TranslatableText("command.suika.warp", new Object[]{player.getDisplayName(), arg}), true);
        else {
            throw warpException.create();
        }
        return 1;
    }

    private static int removeWarp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String arg = WarpPointArgumentType.getWarp(context, "warp");
        WarpPoint.remove(arg);
        return 1;
    }

    private static int setWarp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        String arg = StringArgumentType.getString(context, "warp");
        WarpPoint.addWarp(arg, player);
        return 1;
    }

    private static int warps(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        BaseText text = new TranslatableText("command.suika.warps");
        WarpPoint.warpPoints.keySet().forEach(e -> text.append(e).append(","));
        source.sendFeedback(text, false);
        return 1;
    }
}
