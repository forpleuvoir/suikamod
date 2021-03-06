package com.forpleuvoir.suika.client.commands;

import com.forpleuvoir.suika.client.commands.arguments.FormattingArgumentType;
import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.util.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import static com.forpleuvoir.suika.client.interop.ClientInterop.BASE_COMMAND;
import static com.forpleuvoir.suika.config.ModConfigApp.MOD_CONFIG;
import static com.forpleuvoir.suika.util.TooltipUtil.getKey;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.client.commands
 * @ClassName TooltipCommand
 * @CreateTime 2020/10/19 20:53
 * @Description Tooltip相关指令
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
public class TooltipCommand {
    public static final SimpleCommandExceptionType AIR = new SimpleCommandExceptionType(new TranslatableText("command.suika.tooltip.air"));
    public static final String COMMAND = BASE_COMMAND + "tooltip";
    private static final String ADD = "add";
    private static final String ENABLED = "enabled";
    private static final String REMOVE = "remove";
    private static final String INDEX = "index";
    private static final String ITEM = "item";
    public static final String OWNER = "SkullOwner";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder command = CommandManager.literal(COMMAND)
                .then(add())
                .then(enabled())
                .then(remove());
        dispatcher.register(command);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> add() {
        return CommandManager.literal(ADD)
                .then(CommandManager.argument("formatting", FormattingArgumentType.formatting())
                        .then(CommandManager.argument(ITEM, ItemStackArgumentType.itemStack())
                                .then(CommandManager.argument("text", StringArgumentType.string()).executes(context -> {
                                    ItemStackArgument itemStackArgument = ItemStackArgumentType.getItemStackArgument(context, ITEM);
                                    ItemStack stack = itemStackArgument.createStack(1, false);
                                    Formatting formatting = FormattingArgumentType.getFormatting(context, "formatting");
                                    Item item = itemStackArgument.getItem();
                                    String text = StringArgumentType.getString(context, "text");
                                    if (item.equals(Items.AIR)) {
                                        ClientPlayerEntity player = (ClientPlayerEntity) context.getSource().getEntity();
                                        assert player != null;
                                        stack = player.getMainHandStack();
                                        item = stack.getItem();
                                        if (item.equals(Items.AIR)) {
                                            throw AIR.create();
                                        }
                                    }
                                    String key = getKey(stack);
                                    ConfigManager.getTooltip().addData(key, formatting.toString() + text, true);
                                    result("", stack, " ,tip:" + text, Formatting.AQUA);
                                    return 1;
                                }))
                        )

                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> remove() {
        return CommandManager.literal(REMOVE)
                .then(CommandManager.argument(ITEM, ItemStackArgumentType.itemStack())
                        .then(CommandManager.argument(INDEX, IntegerArgumentType.integer()).executes(context -> {
                            ItemStackArgument itemStackArgument = ItemStackArgumentType.getItemStackArgument(context, ITEM);
                            ItemStack stack = itemStackArgument.createStack(1, false);
                            Item item = itemStackArgument.getItem();
                            int index = IntegerArgumentType.getInteger(context, INDEX) - 1;
                            if (item.equals(Items.AIR)) {
                                ClientPlayerEntity player = (ClientPlayerEntity) context.getSource().getEntity();
                                assert player != null;
                                stack = player.getMainHandStack();
                                item = stack.getItem();
                                if (item.equals(Items.AIR)) {
                                    throw AIR.create();
                                }
                            }
                            String key = getKey(stack);
                            ConfigManager.getTooltip().remove(key, index);
                            result("", stack, " ,remove:" + ++index, Formatting.AQUA);
                            return 1;
                        }))
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> enabled() {
        return CommandManager.literal(ENABLED)
                .then(CommandManager.argument(ENABLED, BoolArgumentType.bool())
                        .executes(context -> {
                            boolean isEnabled = BoolArgumentType.getBool(context, ENABLED);
                            MOD_CONFIG.setTooltip(isEnabled);
                            Formatting formatting = isEnabled ? Formatting.GREEN : Formatting.RED;
                            result("注入:" + isEnabled, formatting);
                            return 0;
                        })
                        .then(CommandManager.argument(ITEM, ItemStackArgumentType.itemStack())
                                .executes(context -> {
                                    boolean isEnabled = BoolArgumentType.getBool(context, ENABLED);
                                    Formatting formatting = isEnabled ? Formatting.GREEN : Formatting.RED;
                                    ItemStackArgument itemStackArgument = ItemStackArgumentType.getItemStackArgument(context, ITEM);
                                    ItemStack stack = itemStackArgument.createStack(1, false);
                                    Item item = itemStackArgument.getItem();
                                    if (item.equals(Items.AIR)) {
                                        ClientPlayerEntity player = (ClientPlayerEntity) context.getSource().getEntity();
                                        assert player != null;
                                        stack = player.getMainHandStack();
                                        item = stack.getItem();
                                        if (item.equals(Items.AIR)) {
                                            throw AIR.create();
                                        }
                                    }
                                    String key = getKey(stack);
                                    ConfigManager.getTooltip().setEnable(key, isEnabled);
                                    result("注入:", stack, ":isEnabled:" + isEnabled, formatting);
                                    return 1;
                                })
                        )
                );
    }


    private static void result(String result, Formatting formatting) {
        CommandUtil.returnFormattingString("Tooltip:" + result, formatting);
    }

    private static void result(String result, ItemStack stack, String append, Formatting formatting) {
        CommandUtil.returnFormattingString("Tooltip:" + result, stack, formatting, append);
    }
}
