package com.forpleuvoir.suika.mixin.client.gui;

import com.forpleuvoir.chatbubbles.FabricModChatBubbles;
import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.config.SuikaConfig;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * 聊天气泡
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.mixin.client.gui
 * @className ClientChatListenerMixin
 * @createTime 2020/10/25 11:04
 */
@Mixin(ChatHudListener.class)
public class ClientChatListenerMixin {
    @Inject(
            method = "onChatMessage",
            at = {@At("HEAD")},
            cancellable = true
    )
    public void postSay(MessageType type, Text text, UUID senderUuid, CallbackInfo ci) {
        if(ConfigManager.getConfig(SuikaConfig.CHAT_BUBBLES,Boolean.class)) {
            FabricModChatBubbles.say(text);
        }
    }
}
