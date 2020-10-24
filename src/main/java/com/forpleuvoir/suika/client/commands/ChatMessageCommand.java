package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.config.SuikaConfig;
import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.client.commands
 * @ClassName ChatMessageCommand
 * @CreateTime 2020/10/19 16:54
 * @Description 聊天注入指令
 */
public class ChatMessageCommand {
    public static final String COMMAND = "cm";
    private static final String ENABLED = "enable";
    private static final String SET = "set";


    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder command = CommandManager.literal(COMMAND)
                .then(enable())
                .then(set());
        dispatcher.register(command);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> enable() {
        return CommandManager.literal(ENABLED)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    // TODO: 2020/10/19 实现注入开启、关闭
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    ConfigManager.setConfig(SuikaConfig.CHAT_MESSAGE, isEnable);

                    Formatting formatting = ConfigManager.getConfigAsBoolean(SuikaConfig.CHAT_MESSAGE) ? Formatting.GREEN : Formatting.RED;
                    result("启用 = " + ConfigManager.getConfigAsBoolean(SuikaConfig.CHAT_MESSAGE), formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> set() {
        return CommandManager.literal(SET).
                then(CommandManager.argument("prefix", StringArgumentType.string())
                        .executes(context -> {
                            // TODO: 2020/10/19 实现前缀注入
                            String prefix = StringArgumentType.getString(context, "prefix");
                            boolean success = ConfigManager.cmConfig.setPrefix(prefix);
                            Formatting formatting = success ? Formatting.GREEN : Formatting.RED;
                            result("注入前缀:" + prefix + " ,成功:" + success, formatting);
                            return 0;
                        })
                        .then(CommandManager.argument("append", StringArgumentType.string())
                                .executes(context -> {
                                    //// TODO: 2020/10/19 实现后缀注入
                                    String prefix = StringArgumentType.getString(context, "prefix");
                                    String append = StringArgumentType.getString(context, "append");
                                    boolean success1 = ConfigManager.cmConfig.setPrefix(prefix);
                                    Formatting formatting1 = success1 ? Formatting.GREEN : Formatting.RED;
                                    boolean success2 = ConfigManager.cmConfig.setAppend(append);
                                    Formatting formatting2 = success2 ? Formatting.GREEN : Formatting.RED;
                                    result("注入前缀:" + prefix + " ,成功:" + success1, formatting1);
                                    result("注入后缀:" + append + " ,成功:" + success2, formatting2);
                                    return 1;
                                })
                        )
                );
    }

    private static void result(String result, Formatting formatting) {
        CommandUtil.returnFormattingString("ChatMessage:" + result, formatting);
    }

}
