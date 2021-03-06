package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;
import static com.forpleuvoir.suika.config.ModConfigApp.MOD_CONFIG;

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
    public static final String GAMMA = BASE_COMMAND + "gamma";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder showEnchantment = showEnchantment();
        LiteralArgumentBuilder autoRebirth = autoRebirth();
        LiteralArgumentBuilder chatBubbles = chatBubbles();
        LiteralArgumentBuilder gamma = CommandManager.literal(GAMMA)
                .then(setGamma())
                .then(setDefValue());
        dispatcher.register(showEnchantment);
        dispatcher.register(autoRebirth);
        dispatcher.register(chatBubbles);
        dispatcher.register(gamma);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> setGamma() {
        return CommandManager.literal("set")
                .then(CommandManager.argument("value", DoubleArgumentType.doubleArg())
                        .executes(context -> {
                            double value = DoubleArgumentType.getDouble(context, "value");
                            MinecraftClient.getInstance().options.gamma = value;
                            result("当前的gamma值:" + value, Formatting.AQUA);
                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> setDefValue() {
        return CommandManager.literal("def")
                .executes(context -> {
                    MinecraftClient.getInstance().options.gamma = 1;
                    result("gamma值已重置", Formatting.AQUA);
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
