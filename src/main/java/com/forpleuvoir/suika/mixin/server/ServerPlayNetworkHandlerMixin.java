package com.forpleuvoir.suika.mixin.server;

import net.minecraft.SharedConstants;
import net.minecraft.client.option.ChatVisibility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.mixin.server
 * @class_name ServerPlayNetworkHandlerMixin
 * @create_time 2021/1/18 13:05
 */

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    public abstract void sendPacket(Packet<?> packet);


    @Shadow
    protected abstract void executeCommand(String input);

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    private int messageCooldown;

    @Shadow public abstract void disconnect(Text reason);

    @Inject(method = "method_31286", at = @At("HEAD"), cancellable = true)
    public void method_31286(String message, CallbackInfo ci) {

        if (this.player.getClientChatVisibility() == ChatVisibility.HIDDEN) {
            sendPacket(new GameMessageS2CPacket((new TranslatableText("chat.cannotSend")).formatted(Formatting.RED), MessageType.SYSTEM, Util.NIL_UUID));
        } else {
            this.player.updateLastActionTime();

            for (int i = 0; i < message.length(); ++i) {
                if (!SharedConstants.isValidChar(message.charAt(i))) {
                    disconnect(new TranslatableText("multiplayer.disconnect.illegal_characters"));
                    return;
                }
            }

            if (message.startsWith("/")) {
                executeCommand(message);
            } else {
                MutableText text = new TranslatableText("chat.type.text", this.player.getDisplayName(), message);
                message = message.replace("&", "§");
                MutableText mutableText = new LiteralText(message);
                if (message.contains("[i]")) {
                    String[] context = message.split("[i]");
                    ItemStack mainHandStack = player.getMainHandStack();
                    MutableText text1 = new LiteralText("");
                    if (!mainHandStack.getItem().equals(Items.AIR)) {
                        for (String s : context) {
                            text1.append(s);
                            if (!s.equals(context[context.length - 1])) {
                                text1.append(mainHandStack.toHoverableText());
                            }
                        }
                        mutableText = text1;
                    }
                }
                text = new TranslatableText("chat.type.text", this.player.getDisplayName(), mutableText);
                this.server.getPlayerManager().broadcastChatMessage(text, MessageType.CHAT, this.player.getUuid());
            }
            this.messageCooldown += 20;
            if (this.messageCooldown > 200 && !this.server.getPlayerManager().isOperator(this.player.getGameProfile())) {
                this.disconnect(new TranslatableText("disconnect.spam"));
            }

        }
        //覆盖原本的方法
        ci.cancel();
    }
}