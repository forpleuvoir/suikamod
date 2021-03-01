package com.forpleuvoir.suika.client.interop;

import com.forpleuvoir.suika.client.commands.ChatMessageCommand;
import com.forpleuvoir.suika.client.commands.RemarkCommand;
import com.forpleuvoir.suika.client.commands.SuikaCommand;
import com.forpleuvoir.suika.client.commands.TooltipCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelonsPackgae com.forpleuvoir.suika.client.interop
 * @ClassName ClientInterop
 * @CreateTime 2020/10/19 15:25
 * @Description 客户端玩家注入
 */
public class ClientInterop {
    public static final String COMMAND_PREFIX = "/";
    public static final String BASE_COMMAND = "suika:";

    public static boolean interceptChatMessage(String message) {
        if (message.startsWith(COMMAND_PREFIX + BASE_COMMAND)) {
            ClientPlayNetworkHandler connection = MinecraftClient.getInstance().getNetworkHandler();
            if (connection != null) {
                CommandDispatcher<CommandSource> commandDispatcher = connection.getCommandDispatcher();
                ServerCommandSource commandSource = MinecraftClient.getInstance().player.getCommandSource();
                try {
                    commandDispatcher.execute(message.substring(1), commandSource);
                } catch (CommandSyntaxException exception) {
                    commandSource.sendError(Texts.toText(exception.getRawMessage()));
                    if (exception.getInput() != null && exception.getCursor() >= 0) {
                        MutableText suggestion = new LiteralText("")
                                .formatted(Formatting.GRAY)
                                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, message)));
                        int textLength = Math.min(exception.getInput().length(), exception.getCursor());
                        if (textLength > 10) {
                            suggestion.append("...");
                        }

                        suggestion.append(exception.getInput().substring(Math.max(0, textLength - 10), textLength));
                        if (textLength < exception.getInput().length()) {
                            suggestion.append(new LiteralText(exception.getInput().substring(textLength))
                                    .formatted(Formatting.RED, Formatting.UNDERLINE));
                        }

                        suggestion.append(new TranslatableText("command.context.here")
                                .formatted(Formatting.RED, Formatting.ITALIC));
                        commandSource.sendError(suggestion);
                    }
                }
            }
            return true;
        }
        return false;
    }


    public static void registerClientCommands(CommandDispatcher<CommandSource> commandDispatcher) {
        ChatMessageCommand.register(commandDispatcher);
        TooltipCommand.register(commandDispatcher);
        SuikaCommand.register(commandDispatcher);
        RemarkCommand.register(commandDispatcher);
    }

}
