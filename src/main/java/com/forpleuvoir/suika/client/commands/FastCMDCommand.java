package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.client.SuikaClient;
import com.forpleuvoir.suika.client.gui.FastCommandScreen;
import com.forpleuvoir.suika.client.config.ConfigManager;
import com.forpleuvoir.suika.client.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;
import static net.minecraft.command.CommandSource.suggestMatching;

/**
 * #package com.forpleuvoir.suika.client.commands
 * #class_name FastCMDCommand
 * #create_time 2021/3/6 15:33
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class FastCMDCommand {

    public static final String FAST_CMD = BASE_COMMAND + "fast_command";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder cmd = cmd();
        dispatcher.register(cmd);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> cmd() {
        return CommandManager.literal(FAST_CMD)
                .then(set())
                .then(del())
                .then(rename())
                .then(editScreen());
    }

    private static LiteralArgumentBuilder<ServerCommandSource> set() {
        return CommandManager.literal("set")
                .then(CommandManager.argument("remark", StringArgumentType.string())
                        .then(CommandManager.argument("command", StringArgumentType.string())
                                .executes(context -> {
                                    String key = StringArgumentType.getString(context, "remark");
                                    String value = StringArgumentType.getString(context, "command");
                                    ConfigManager.getFastCommand().add(key, value);
                                    result(Formatting.AQUA, "添加了新的快捷指令:[" + key + "](" + value + ")");
                                    return 1;
                                })
                        )
                );

    }

    private static LiteralArgumentBuilder<ServerCommandSource> del() {
        return CommandManager.literal("del")
                .then(CommandManager.argument("remark", StringArgumentType.string())
                        .suggests((c, b) ->
                                suggestMatching(ConfigManager.getFastCommand().getKeys(), b)
                        )
                        .executes(context -> {
                            String key = StringArgumentType.getString(context, "remark");
                            boolean delete = ConfigManager.getFastCommand().delete(key);
                            if (delete)
                                result(Formatting.RED, "删除快捷指令:[" + key + "]");
                            else
                                result(Formatting.RED, "没有找到指令:[" + key + "]");
                            return 1;
                        })
                );

    }

    private static LiteralArgumentBuilder<ServerCommandSource> rename() {
        return CommandManager.literal("rename")
                .then(CommandManager.argument("old_remark", StringArgumentType.string())
                        .suggests((c, b) ->
                                suggestMatching(ConfigManager.getFastCommand().getKeys(), b)
                        )
                        .then(CommandManager.argument("new_remark", StringArgumentType.string())
                                .executes(context -> {
                                    String oldRemark = StringArgumentType.getString(context, "old_remark");
                                    String newRemark = StringArgumentType.getString(context, "new_remark");
                                    ConfigManager.getFastCommand().rename(oldRemark,newRemark);
                                    result(Formatting.AQUA, "重命名快捷指令:[" + oldRemark + "]to[" + newRemark + "]");
                                    return 1;
                                })
                        )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> editScreen(){
        return CommandManager.literal("editScreen").executes(context -> {
            SuikaClient.addTask(client -> {
                client.openScreen(new FastCommandScreen(FastCommandScreen.Mode.EDIT));
            });
            return 1;
        });
    }

    private static void result(Formatting formatting, String text) {
        CommandUtil.returnFormattingString("FastCommand:" + text, formatting);
    }
}
