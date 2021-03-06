package com.forpleuvoir.suika.config;

import com.forpleuvoir.suika.Suika;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.config
 * @class_name HotKeys
 * @create_time 2020/11/3 20:44
 */

public class HotKeys {
    public static KeyBinding CUSTOM_CHAT_MESSAGE_KEY=get("custom_chat_message", GLFW.GLFW_KEY_B);
    public static KeyBinding FAST_COMMAND=get("fast_command", GLFW.GLFW_KEY_X);

    public static void register() {
        KeyBindingHelper.registerKeyBinding(CUSTOM_CHAT_MESSAGE_KEY);
        KeyBindingHelper.registerKeyBinding(FAST_COMMAND);
    }


    private static KeyBinding get(String key, Integer code) {
        return new KeyBinding("key." + Suika.MOD_ID + "." + key, InputUtil.Type.KEYSYM, code, "suika mod");
    }
}
