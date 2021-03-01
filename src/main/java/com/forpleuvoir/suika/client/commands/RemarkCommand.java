package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.util.CommandUtil;
import com.forpleuvoir.suika.util.PlayerHeadUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;

/**
 * #package com.forpleuvoir.suika.client.commands
 * #class_name RemarkCommand
 * #create_time 2021/3/1 17:34
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class RemarkCommand {
    public static final String REMARK = BASE_COMMAND + "remark";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder remark = remark();
        dispatcher.register(remark);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> remark() {
        return CommandManager.literal(REMARK)
                .then(CommandManager.argument("player", StringArgumentType.string())
                        .then(set())
                        .then(del())
                );
    }


    private static LiteralArgumentBuilder<ServerCommandSource> set() {
        return CommandManager.literal("set")
                .then(CommandManager.argument("remark", StringArgumentType.string())
                        .executes(context -> {
                            String player = StringArgumentType.getString(context, "player");
                            String remark = StringArgumentType.getString(context, "remark");
                            PlayerHeadUtil.loadProperties(new GameProfile(null, player), object -> {
                                GameProfile profile = (GameProfile) object;
                                if (ConfigManager.getRemark().set(profile, remark)) {
                                    assert profile != null;
                                    result(Formatting.AQUA, "给玩家" + profile.getName() + "添加备注:" + remark);
                                }
                            });
                            return 1;
                        })
                );

    }

    private static LiteralArgumentBuilder<ServerCommandSource> del() {
        return CommandManager.literal("del").executes(context -> {
            String player = StringArgumentType.getString(context, "player");
            PlayerHeadUtil.loadProperties(new GameProfile(null, player), object -> {
                GameProfile profile = (GameProfile) object;
                if (ConfigManager.getRemark().remove(profile)) {
                    assert profile != null;
                    result(Formatting.RED, "删除玩家" + profile.getName() + "的备注");
                }
            });
            return 1;
        });

    }

    private static void result(Formatting formatting, String text) {
        CommandUtil.returnFormattingString("Remark:" + text, formatting);
    }


}
