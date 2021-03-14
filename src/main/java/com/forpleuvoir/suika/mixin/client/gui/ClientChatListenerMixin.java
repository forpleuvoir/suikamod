package com.forpleuvoir.suika.mixin.client.gui;

import com.forpleuvoir.chatbubbles.FabricModChatBubbles;
import com.forpleuvoir.suika.client.config.ConfigManager;
import com.forpleuvoir.suika.util.Log;
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

import static com.forpleuvoir.suika.client.config.ModConfigApp.MOD_CONFIG;

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

    private final Log log=new Log(ClientChatListenerMixin.class);

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "onChatMessage",
            at = {@At("HEAD")},
            cancellable = true
    )
    public void postSay(MessageType type, Text text, UUID senderUuid, CallbackInfo ci) {
        if (MOD_CONFIG.getChatBubbles()) {
            FabricModChatBubbles.say(text);
        }
        if (MOD_CONFIG.getRemarkPlayer()) {
            log.info("text : " + text.asString(), "uuid : " + senderUuid.toString());
            if (type != MessageType.CHAT) {
                this.client.inGameHud.getChatHud().addMessage(text);
            } else {
                Text text1 = ConfigManager.getRemark().build(text, senderUuid);
                log.info("build text : "+text.asString());
                this.client.inGameHud.getChatHud().queueMessage(text1);
            }
            ci.cancel();
        }
    }
}
