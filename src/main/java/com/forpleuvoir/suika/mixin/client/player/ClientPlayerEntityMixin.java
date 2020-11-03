package com.forpleuvoir.suika.mixin.client.player;


import com.forpleuvoir.suika.client.interop.ClientInterop;
import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.config.SuikaConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.forpleuvoir.suika.client.interop.ClientInterop.COMMAND_PREFIX;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.mixin.client.player
 * @ClassName ClientPlayerEntityMixin
 * @CreateTime 2020/10/19 15:25
 * @Description 客户端玩家注入
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Final
    @Shadow
    public ClientPlayNetworkHandler networkHandler;
    @Shadow
    @Final
    protected MinecraftClient client;

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        if (ClientInterop.interceptChatMessage(message)) {
            ci.cancel();
        }
        if (!message.startsWith(COMMAND_PREFIX)) {
            if (ConfigManager.getConfig(SuikaConfig.CHAT_MESSAGE, Boolean.class)) {
                String msg;
                String[] cm = ConfigManager.getChatMessage();
                msg = cm[0] + message + cm[1];
                this.networkHandler.sendPacket(new ChatMessageC2SPacket(msg));
                ci.cancel();
            }
        }
    }

    @Inject(method = "showsDeathScreen", at = @At("HEAD"), cancellable = true)
    public void showsDeathScreen(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!ConfigManager.getConfig(SuikaConfig.AUTO_REBIRTH, Boolean.class));
    }

}
