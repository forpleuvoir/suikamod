package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.config.SuikaConfig;
import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;

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
                    ConfigManager.setConfig(SuikaConfig.SHOW_ENCHANTMENT, isEnable);
                    Formatting formatting = ConfigManager.getConfig(SuikaConfig.SHOW_ENCHANTMENT, Boolean.class) ? Formatting.GREEN : Formatting.RED;
                    result("HeldItemTooltip 启用 = " + ConfigManager.getConfig(SuikaConfig.SHOW_ENCHANTMENT, Boolean.class), formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> autoRebirth() {
        return CommandManager.literal(AUTO_REBIRTH)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    ConfigManager.setConfig(SuikaConfig.AUTO_REBIRTH, isEnable);
                    Formatting formatting = ConfigManager.getConfig(SuikaConfig.AUTO_REBIRTH, Boolean.class) ? Formatting.GREEN : Formatting.RED;
                    result("AutoRebirth = " + ConfigManager.getConfig(SuikaConfig.AUTO_REBIRTH, Boolean.class), formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> chatBubbles() {
        return CommandManager.literal(CHAT_BUBBLES)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    ConfigManager.setConfig(SuikaConfig.CHAT_BUBBLES, isEnable);
                    Formatting formatting = ConfigManager.getConfig(SuikaConfig.CHAT_BUBBLES, Boolean.class) ? Formatting.GREEN : Formatting.RED;
                    result("ChatBubbles = " + ConfigManager.getConfig(SuikaConfig.CHAT_BUBBLES, Boolean.class), formatting);
                    return 1;
                }));
    }

    private static void result(String result, Formatting formatting) {
        CommandUtil.returnFormattingString("Suika Mod:" + result, formatting);
    }
}
