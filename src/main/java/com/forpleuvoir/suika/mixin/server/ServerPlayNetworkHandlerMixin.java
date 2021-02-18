package com.forpleuvoir.suika.mixin.server;

import net.minecraft.client.option.ChatVisibility;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.TextStream;
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

    @Shadow
    public abstract void disconnect(Text reason);

    @Inject(method = "method_31286", at = @At("HEAD"), cancellable = true)
    public void method_31286(TextStream.class_5837 arg, CallbackInfo ci) {
        if (this.player.getClientChatVisibility() == ChatVisibility.HIDDEN) {
            this.sendPacket(new GameMessageS2CPacket((new TranslatableText("chat.disabled.options")).formatted(Formatting.RED), MessageType.SYSTEM, Util.NIL_UUID));
        } else {
            this.player.updateLastActionTime();
            String string = arg.method_33801();
            if (string.startsWith("/")) {
                this.executeCommand(string);
            } else {
                String string2 = arg.method_33803();
                string2 = string2.replace("&", "§");
                MutableText mutableText = new LiteralText("");
                if(string2.contains("[i]")) {
                    String[] s = string2.split("\\[i]",-1);
                    for (int i = 0; i < s.length; i++) {
                        mutableText.append(s[i]);
                        if (i != s.length - 1) {
                            ItemStack stack = this.player.getMainHandStack();
                            if (stack.getItem() != Items.AIR) {
                                mutableText.append(stack.toHoverableText());
                            } else {
                                mutableText.append("[i]");
                            }
                        }
                    }
                }else{
                    mutableText.append(string2);
                }
                Text text = string2.isEmpty() ? null : new TranslatableText("chat.type.text", new Object[]{this.player.getDisplayName()}).append(mutableText);
                Text text2 = new TranslatableText("chat.type.text", new Object[]{this.player.getDisplayName()}).append(mutableText);

                this.server.getPlayerManager().method_33810(text2, (serverPlayerEntity) -> this.player.method_33795(serverPlayerEntity) ? text : text2, MessageType.CHAT, this.player.getUuid());
            }

            this.messageCooldown += 20;
            if (this.messageCooldown > 200 && !this.server.getPlayerManager().isOperator(this.player.getGameProfile())) {
                this.disconnect(new TranslatableText("disconnect.spam"));
            }

        }
        ci.cancel();
    }
}