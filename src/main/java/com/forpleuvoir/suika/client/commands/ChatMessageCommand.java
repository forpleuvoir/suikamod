package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;
import static com.forpleuvoir.suika.config.ModConfigApp.modConfig;

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


    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder command = CommandManager.literal(COMMAND)
                .then(enable())
                .then(set());
        dispatcher.register(command);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> enable() {
        return CommandManager.literal(ENABLED)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                            modConfig.setChatMessage(isEnable);
                            Formatting formatting = modConfig.getChatMessage() ? Formatting.GREEN : Formatting.RED;
                            result("启用 = " + modConfig.getChatMessage(), formatting);
                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> set() {
        return CommandManager.literal(SET)
                .then(CommandManager.argument("prefix", StringArgumentType.string())
                        .then(CommandManager.argument("append", StringArgumentType.string())
                                .executes(context -> {
                                    String prefix = StringArgumentType.getString(context, "prefix");
                                    String append = StringArgumentType.getString(context, "append");
                                    modConfig.setChatMessagePrefix(prefix);
                                    modConfig.setChatMessageAppend(append);
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
