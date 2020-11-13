package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;
import static com.forpleuvoir.suika.config.ModConfigApp.modConfig;

/**
 * 乱七八糟的指令
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.client.commands
 * @className SuikaCommand
 * @createTime 2020/10/22 12:08
 */
public class SuikaCommand {
    public static final String SHOW_ENCHANTMENT = BASE_COMMAND + "show_enchantment";
    public static final String AUTO_REBIRTH = BASE_COMMAND + "auto_rebirth";
    public static final String CHAT_BUBBLES = BASE_COMMAND + "chat_bubbles";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder showEnchantment = showEnchantment();
        LiteralArgumentBuilder autoRebirth = autoRebirth();
        LiteralArgumentBuilder chatBubbles = chatBubbles();
        dispatcher.register(showEnchantment);
        dispatcher.register(autoRebirth);
        dispatcher.register(chatBubbles);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> showEnchantment() {
        return CommandManager.literal(SHOW_ENCHANTMENT)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    modConfig.setShowEnchantment(isEnable);
                    Formatting formatting = modConfig.getShowEnchantment() ? Formatting.GREEN : Formatting.RED;
                    result("HeldItemTooltip 启用 = " + modConfig.getShowEnchantment(), formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> autoRebirth() {
        return CommandManager.literal(AUTO_REBIRTH)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    modConfig.setAutoRebirth(isEnable);
                    Formatting formatting = modConfig.getAutoRebirth() ? Formatting.GREEN : Formatting.RED;
                    result("AutoRebirth = " + modConfig.getAutoRebirth(), formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> chatBubbles() {
        return CommandManager.literal(CHAT_BUBBLES)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    modConfig.setChatBubbles(isEnable);
                    Formatting formatting = modConfig.getChatBubbles() ? Formatting.GREEN : Formatting.RED;
                    result("ChatBubbles = " + modConfig.getChatBubbles(), formatting);
                    return 1;
                }));
    }

    private static void result(String result, Formatting formatting) {
        CommandUtil.returnFormattingString("Suika Mod:" + result, formatting);
    }
}
