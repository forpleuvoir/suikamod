package com.forpleuvoir.suika.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

/**
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.util
 * @ClassName CommandUtil
 * @author forpleuvoir
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
}
