package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.client.SuikaClient;
import com.forpleuvoir.suika.client.data.TimeTask;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;

/**
 * #package com.forpleuvoir.suika.client.commands
 * #class_name TaskCommand
 * #create_time 2021/3/19 13:52
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class TaskCommand {

    private static final String BASE = BASE_COMMAND + "task";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder base = base();
        dispatcher.register(base);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> base() {
        return CommandManager.literal(BASE)
                .then(add());
    }

    private static LiteralArgumentBuilder<ServerCommandSource> add() {
        return CommandManager.literal("add")
                .then(runCommand());
    }

    private static LiteralArgumentBuilder<ServerCommandSource> runCommand() {
        return CommandManager.literal("send_message")
                .then(CommandManager.argument("num", IntegerArgumentType.integer(-1))
                        .then(CommandManager.argument("time", IntegerArgumentType.integer())
                                .then(CommandManager.argument("content", StringArgumentType.string())
                                        .executes(context -> {
                                            int num = IntegerArgumentType.getInteger(context, "num");
                                            int time = IntegerArgumentType.getInteger(context, "time");
                                            String content = StringArgumentType.getString(context, "content");
                                            TimeTask timeTask = new TimeTask(num, time, TimeTask.Type.SEND_MESSAGE, content);
                                            SuikaClient.addTimeTask(timeTask);
                                            return 1;
                                        })
                                )
                        )
                );
    }
}
