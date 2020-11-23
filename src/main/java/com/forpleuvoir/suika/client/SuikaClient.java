package com.forpleuvoir.suika.client;

import com.forpleuvoir.suika.Suika;
import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.config.HotKeys;
import com.forpleuvoir.suika.config.ModConfigApp;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.util.Objects;

import static com.forpleuvoir.suika.config.ModConfigApp.modConfig;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelonsPackge com.forpleuvoir.suika.client
 * @ClassName SuikaClient
 * @CreateTime 2020/10/19 11:44
 * @Description 客户端初始化类
 */
@Environment(EnvType.CLIENT)
public class SuikaClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        Suika.LOGGER.info("suika mod initializeClient...");
        ModConfigApp.init();
        HotKeys.register();
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        ConfigManager.init();
    }

    public void tick(MinecraftClient client) {
        if (modConfig.getCustomChatMessage())
            if (HotKeys.CUSTOM_CHAT_MESSAGE_KEY.wasPressed()) {
                ((ClientPlayerEntity) Objects.requireNonNull(client.getCameraEntity())).networkHandler.sendPacket(new ChatMessageC2SPacket(modConfig.getCustomChatMessageValue()));
            }
    }
}
