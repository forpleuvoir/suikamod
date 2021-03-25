package com.forpleuvoir.suika.client.config;

import com.forpleuvoir.suika.client.Suika;
import com.forpleuvoir.suika.client.gui.FastCommandScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static com.forpleuvoir.suika.client.config.ModConfigApp.MOD_CONFIG;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.client.config
 * @class_name HotKeys
 * @create_time 2020/11/3 20:44
 */

public class HotKeys {
    public static KeyBinding CUSTOM_CHAT_MESSAGE_KEY = get("custom_chat_message", GLFW.GLFW_KEY_B);
    public static KeyBinding FAST_COMMAND = get("fast_command", GLFW.GLFW_KEY_X);

    public static void register() {
        KeyBindingHelper.registerKeyBinding(CUSTOM_CHAT_MESSAGE_KEY);
        KeyBindingHelper.registerKeyBinding(FAST_COMMAND);
    }

    private static KeyBinding get(String key, Integer code) {
        return new KeyBinding("key." + Suika.MOD_ID + "." + key, InputUtil.Type.KEYSYM, code, "suika mod");
    }

    public static void tick(MinecraftClient client) {
        if (MOD_CONFIG.getCustomChatMessage())
            if (HotKeys.CUSTOM_CHAT_MESSAGE_KEY.wasPressed()) {
                ((ClientPlayerEntity) Objects.requireNonNull(client.getCameraEntity())).networkHandler.sendPacket(new ChatMessageC2SPacket(MOD_CONFIG.getCustomChatMessageValue()));
            }
        if (HotKeys.FAST_COMMAND.wasPressed()) {
            if (MOD_CONFIG.getFastCommandGui()) {
                MinecraftClient.getInstance().openScreen(new FastCommandScreen(FastCommandScreen.Mode.RUN));
            } else {
                ConfigManager.getFastCommand().show();
                MinecraftClient.getInstance().openScreen(new ChatScreen(""));
            }
        }
    }

}
