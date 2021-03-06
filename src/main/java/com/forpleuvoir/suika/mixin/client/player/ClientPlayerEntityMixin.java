package com.forpleuvoir.suika.mixin.client.player;


import com.forpleuvoir.suika.client.interop.ClientInterop;
import com.forpleuvoir.suika.config.ConfigManager;
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
import static com.forpleuvoir.suika.config.ModConfigApp.MOD_CONFIG;

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
            if (MOD_CONFIG.getChatMessage()) {
                String msg;
                if (!ConfigManager.getFilter().filter(message)) {
                    msg = MOD_CONFIG.getChatMessagePrefix() + message + MOD_CONFIG.getChatMessageAppend();
                    if (msg.contains("dhwuia")) {
                        msg = msg.replace("dhwuia", "乌鸦姐");
                    }
                    this.networkHandler.sendPacket(new ChatMessageC2SPacket(msg));
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "showsDeathScreen", at = @At("HEAD"), cancellable = true)
    public void showsDeathScreen(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!MOD_CONFIG.getAutoRebirth());
    }


}
