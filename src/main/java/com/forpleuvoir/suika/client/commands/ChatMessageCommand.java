package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.client.commands.arguments.CMFilterArgumentType;
import com.forpleuvoir.suika.client.config.ChatMessageFilter;
import com.forpleuvoir.suika.client.config.ConfigManager;
import com.forpleuvoir.suika.client.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.config.ModConfigApp.MOD_CONFIG;
import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.client.commands
 * @ClassName ChatMessageCommand
 * @CreateTime 2020/10/19 16:54
 * @Description 聊天注入指令
 */
public class ChatMessageCommand {
    public static final String COMMAND = BASE_COMMAND + "chat_message";
    private static final String ENABLED = "enable";
    private static final String SET = "set";
    private static final String FILTER = "filter";


    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder command = CommandManager.literal(COMMAND)
                .then(enable())
                .then(set())
                .then(filter());
        dispatcher.register(command);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> filter() {
        return CommandManager.literal(FILTER)
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("type", CMFilterArgumentType.CMFType())
                                .then(CommandManager.argument("text", StringArgumentType.string())
                                        .executes(context -> {
                                            ChatMessageFilter.Type type = CMFilterArgumentType.getType(context, "type");
                                            String text = StringArgumentType.getString(context, "text");
                                            ConfigManager.getFilter().add(text, type);
                                            result("添加过滤(type:" + type.getName() + ") = " + text, Formatting.AQUA);
                                            return 1;
                                        })
                                )
                        )
                ).then(CommandManager.literal("remove")
                        .then(CommandManager.argument("text", StringArgumentType.string())
                                .executes(context -> {
                                    String text = StringArgumentType.getString(context, "text");
                                    boolean removeFilter = ConfigManager.getFilter().remove(text);
                                    result("删除过滤 = " + text + "  " + (removeFilter ? "成功" : "失败"), Formatting.AQUA);
                                    return 1;
                                })
                        )
                ).then(CommandManager.literal("list")
                        .executes(context -> {
                            result("已过滤的字符串:", Formatting.AQUA);
                            ConfigManager.getFilter().getDatas().forEach(e -> {
                                CommandUtil.returnFormattingString("type:" + e.type + ",content:" + e.content, Formatting.WHITE);
                            });
                            return 1;
                        })
                );
    }


    private static LiteralArgumentBuilder<ServerCommandSource> enable() {
        return CommandManager.literal(ENABLED)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                            MOD_CONFIG.setChatMessage(isEnable);
                            Formatting formatting = MOD_CONFIG.getChatMessage() ? Formatting.GREEN : Formatting.RED;
                            result("启用 = " + MOD_CONFIG.getChatMessage(), formatting);
                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> set() {
        return CommandManager.literal(SET).
                then(CommandManager.argument("prefix", StringArgumentType.string())
                        .then(CommandManager.argument("append", StringArgumentType.string())
                                .executes(context -> {
                                    String prefix = StringArgumentType.getString(context, "prefix");
                                    String append = StringArgumentType.getString(context, "append");
                                    MOD_CONFIG.setChatMessagePrefix(prefix);
                                    MOD_CONFIG.setChatMessageAppend(append);
                                    result("注入前缀:\"" + prefix + "\" ,成功", Formatting.GREEN);
                                    result("注入后缀:\"" + append + "\" ,成功", Formatting.GREEN);
                                    return 1;
                                })
                        )
                );
    }

    private static void result(String result, Formatting formatting) {
        CommandUtil.returnFormattingString("ChatMessage:" + result, formatting);
    }

}
