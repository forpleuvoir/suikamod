package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.client.SuikaClient;
import com.forpleuvoir.suika.client.config.ConfigManager;
import com.forpleuvoir.suika.client.gui.SuikaConfigScreen;
import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;
import static com.forpleuvoir.suika.client.config.ModConfigApp.MOD_CONFIG;

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
    public static final String SETTING = BASE_COMMAND + "setting";
    public static final String RELOAD = BASE_COMMAND + "reload";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder showEnchantment = showEnchantment();
        LiteralArgumentBuilder autoRebirth = autoRebirth();
        LiteralArgumentBuilder chatBubbles = chatBubbles();
        LiteralArgumentBuilder setting = setting();
        LiteralArgumentBuilder reload = reload();
        dispatcher.register(showEnchantment);
        dispatcher.register(autoRebirth);
        dispatcher.register(chatBubbles);
        dispatcher.register(setting);
        dispatcher.register(reload);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> setting() {
        return CommandManager.literal(SETTING).executes(context -> {
            SuikaClient.addTask(client -> client.openScreen(SuikaConfigScreen.initScreen(null)));
            return 1;
        });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> reload() {
        return CommandManager.literal(RELOAD).executes(context -> {
            SuikaClient.addTask(client -> ConfigManager.init());
            return 1;
        });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> showEnchantment() {
        return CommandManager.literal(SHOW_ENCHANTMENT)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    MOD_CONFIG.setShowEnchantment(isEnable);
                    Formatting formatting = MOD_CONFIG.getShowEnchantment() ? Formatting.GREEN : Formatting.RED;
                    result("HeldItemTooltip 启用 = " + MOD_CONFIG.getShowEnchantment(), formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> autoRebirth() {
        return CommandManager.literal(AUTO_REBIRTH)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    MOD_CONFIG.setAutoRebirth(isEnable);
                    Formatting formatting = MOD_CONFIG.getAutoRebirth() ? Formatting.GREEN : Formatting.RED;
                    result("AutoRebirth = " + MOD_CONFIG.getAutoRebirth(), formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> chatBubbles() {
        return CommandManager.literal(CHAT_BUBBLES)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    MOD_CONFIG.setChatBubbles(isEnable);
                    Formatting formatting = MOD_CONFIG.getChatBubbles() ? Formatting.GREEN : Formatting.RED;
                    result("ChatBubbles = " + MOD_CONFIG.getChatBubbles(), formatting);
                    return 1;
                }));
    }

    private static void result(String result, Formatting formatting) {
        CommandUtil.returnFormattingString("Suika Mod:" + result, formatting);
    }
}
