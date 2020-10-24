package com.forpleuvoir.suika.mixin.client.player;


import com.forpleuvoir.suika.client.interop.ClientInterop;
import com.forpleuvoir.suika.config.ConfigManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.mixin.client.player
 * @ClassName ClientPlayerEntityMixin
 * @CreateTime 2020/10/19 15:25
 * @Description 客户端玩家注入
 */
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    @Final
    @Shadow
    public ClientPlayNetworkHandler networkHandler;

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        if (ClientInterop.interceptChatMessage(message)) {
            ci.cancel();
            return;
        }
        String msg = message;
        if (!message.startsWith(ClientInterop.COMMAND_PREFIX)) {
            if (ConfigManager.cmConfig.isEnabled()) {
                String prefix = ConfigManager.cmConfig.getPrefix();
                String append = ConfigManager.cmConfig.getAppend();
                msg = prefix + message + append;
            }
        }
        this.networkHandler.sendPacket(new ChatMessageC2SPacket(msg));
        ci.cancel();
    }

    @Inject(method = "showsDeathScreen", at = @At("HEAD"), cancellable = true)
    public void showsDeathScreen(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!ConfigManager.autoRebirth);
    }

}
