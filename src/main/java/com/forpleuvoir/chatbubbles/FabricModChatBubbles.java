package com.forpleuvoir.chatbubbles;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

/**
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.chatbubbles
 * @className FabricModChatBubbles
 * @createTime 2020/10/25 11:41
 */
public class FabricModChatBubbles implements ClientModInitializer {
    static FabricModChatBubbles instance;
    MinecraftClient minecraft;
    ChatBubbles chatBubbles;
    private boolean initialized = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        instance = this;
    }

    public void lateInit() {
        this.minecraft = MinecraftClient.getInstance();
        this.chatBubbles = new ChatBubbles();
    }

    public void tick(MinecraftClient client) {
        if (!this.initialized) {
            if (client != null && client.getOverlay () == null) {
                this.lateInit();
                this.initialized = true;
            }
        } else {
            Entity renderViewEntity = this.minecraft.getCameraEntity ();
            boolean inGame = renderViewEntity != null && renderViewEntity.world  != null;
            if (inGame) {
                this.chatBubbles.onTickInGame(this.minecraft);
            }
        }

    }
    public static void say(Text textComponent) {
        instance.chatBubbles.clientString(textComponent.getString());
    }
}
