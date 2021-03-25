package com.forpleuvoir.suika.client.mixin.network.play.server;

import com.forpleuvoir.suika.client.interop.ClientInterop;
import com.forpleuvoir.suika.client.common.TypeHelper;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.client.mixin.network.play.server
 * @ClassName SCommandListPacketMixin
 * @author forpleuvoir
 * @CreateTime 2020/10/19 17:29
 * @Description 客户端命令注入
 */
@Mixin(CommandTreeS2CPacket.class)
public abstract class SCommandListPacketMixin {
    @Inject(method = "apply", at = @At("RETURN"))
    private void processPacket(ClientPlayPacketListener netHandlerPlayClient, CallbackInfo ci) {
        TypeHelper.doIfType(netHandlerPlayClient, ClientPlayNetworkHandler.class, handler ->
                ClientInterop.registerClientCommands(handler.getCommandDispatcher())
        );
    }
}
