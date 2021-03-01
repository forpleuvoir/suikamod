package com.forpleuvoir.suika.mixin.client.gui;

import com.forpleuvoir.chatbubbles.FabricModChatBubbles;
import com.forpleuvoir.suika.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static com.forpleuvoir.suika.config.ModConfigApp.modConfig;

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
public abstract class ClientChatListenerMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "onChatMessage",
            at = {@At("HEAD")},
            cancellable = true
    )
    public void postSay(MessageType type, Text text
            , UUID senderUuid, CallbackInfo ci) {
        if (modConfig.getChatBubbles()) {
            FabricModChatBubbles.say(text);
        }
        if(modConfig.getRemarkPlayer()) {
            System.out.println(text.getString());
            System.out.println(type);
            System.out.println(senderUuid.toString());
            if (type != MessageType.CHAT) {
                this.client.inGameHud.getChatHud().addMessage(text);
            } else {
                Text text1 = ConfigManager.getRemark().build(text, senderUuid);
                this.client.inGameHud.getChatHud().queueMessage(text1);
                ci.cancel();
            }
        }
    }
}
