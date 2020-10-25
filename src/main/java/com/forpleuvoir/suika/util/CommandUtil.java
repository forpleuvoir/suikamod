package com.forpleuvoir.suika.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.util
 * @ClassName CommandUtil
 * @CreateTime 2020/10/19 20:44
 * @Description 指令工具
 */
public class CommandUtil {
    public static void returnFormattingString(String result, Formatting formatting) {
        String prefix = "";
        if (formatting != null) {
            prefix = formatting.toString();
        }
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(prefix + result));
    }

    public static void returnFormattingString(String key, ItemStack stack, Formatting formatting,String append) {
        String prefix = "";
        if (formatting != null) {
            prefix = formatting.toString();
        }
        Text result = Texts.bracketed(new LiteralText(prefix+key)).append(stack.toHoverableText()).append(append);
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(result);
    }
}
