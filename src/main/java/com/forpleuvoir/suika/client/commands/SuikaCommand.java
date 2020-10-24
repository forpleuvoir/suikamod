package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

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

    public static final String COMMAND = "suika";
    public static final String HITT = "hitt";
    public static final String AUTO_REBIRTH="auto_rebirth";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder command = CommandManager.literal(COMMAND)
                .then(hit())
                .then(autoRebirth());
        dispatcher.register(command);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> hit() {
        return CommandManager.literal(HITT)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    // TODO: 2020/10/19 实现注入开启、关闭
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    ConfigManager.hit =isEnable;
                    Formatting formatting = ConfigManager.hit ? Formatting.GREEN : Formatting.RED;
                    result("HeldItemTooltip 启用 = " + ConfigManager.hit, formatting);
                    return 1;
                }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> autoRebirth() {
        return CommandManager.literal(AUTO_REBIRTH)
                .then(CommandManager.argument("isEnabled", BoolArgumentType.bool()).executes(context -> {
                    // TODO: 2020/10/19 实现注入开启、关闭
                    boolean isEnable = BoolArgumentType.getBool(context, "isEnabled");
                    ConfigManager.autoRebirth =isEnable;
                    Formatting formatting = ConfigManager.autoRebirth ? Formatting.GREEN : Formatting.RED;
                    result("AutoRebirth = " + ConfigManager.autoRebirth, formatting);
                    return 1;
                }));
    }

    private static void result(String result, Formatting formatting) {
        CommandUtil.returnFormattingString("Suika Mod:" + result, formatting);
    }
}
