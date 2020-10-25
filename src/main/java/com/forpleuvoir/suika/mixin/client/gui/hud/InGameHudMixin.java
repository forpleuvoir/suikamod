package com.forpleuvoir.suika.mixin.client.gui.hud;

/**
 * 后面再说
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.mixin.client.gui.hud
 * @className InGameHudMixin
 * @createTime 2020/10/22 9:49
 */

import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.config.SuikaConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

import static com.forpleuvoir.suika.util.TooltipUtil.addTooltip;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int heldItemTooltipFade;
    @Shadow
    private ItemStack currentStack;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    public void renderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci) {
        this.client.getProfiler().push("selectedItemName");
        if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
            MutableText mutableText = (new LiteralText("")).append(this.currentStack.getName()).formatted(this.currentStack.getRarity().formatting);
            if (this.currentStack.hasCustomName()) {
                mutableText.formatted(Formatting.ITALIC);
            }
            LinkedList<MutableText> list = new LinkedList<>();
            list.addFirst(mutableText);
            if(ConfigManager.getConfig(SuikaConfig.SHOW_ENCHANTMENT,Boolean.class)) {
                addTooltip(currentStack, list);
                if (!currentStack.getEnchantments().isEmpty()) {
                    for (int i = 0; i < currentStack.getEnchantments().size(); i++) {
                        CompoundTag compoundTag = currentStack.getEnchantments().getCompound(i);
                        int lvl = compoundTag.getInt("lvl");
                        String str = compoundTag.getString("id");
                        String key = "enchantment." + str.split(":")[0] + "." + str.split(":")[1];
                        MutableText e = new TranslatableText(key).append(" ");
                        if(lvl!=1) {
                            e.append(new TranslatableText("enchantment.level." + lvl));
                        }
                        e.formatted(Formatting.DARK_AQUA);
                        list.addLast(e);
                    }
                }
            }
            int i = this.getFontRenderer().getWidth((StringVisitable) mutableText);
            int j = (this.scaledWidth - i) / 2;
            int k = this.scaledHeight - 59;
            if (!this.client.interactionManager.hasStatusBars()) {
                k += 14;
            }

            int l = (int) ((float) this.heldItemTooltipFade * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            if (l > 0) {
                RenderSystem.pushMatrix();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                int var10001 = j - 2;
                int var10002 = k - 2;
                int var10003 = j + i + 2;
                this.getFontRenderer().getClass();
                DrawableHelper.fill(matrices, var10001, var10002, var10003, k + 9 + 2, this.client.options.getTextBackgroundColor(0));
                int count = 0;
                int padding = 10;
                int size = list.size();
                int newK = k - ((size - 1) * padding);
                for (MutableText e : list) {
                    int a = this.getFontRenderer().getWidth((StringVisitable) e);
                    int b = (this.scaledWidth - a) / 2;
                    this.getFontRenderer().drawWithShadow(matrices, (Text) e, (float) b, (float) newK + (count*padding), 16777215 + (l << 24));
                    count++;
                }
                RenderSystem.disableBlend();
                RenderSystem.popMatrix();
            }
        }
        this.client.getProfiler().pop();
        ci.cancel();
        return;
    }

    public TextRenderer getFontRenderer() {
        return this.client.textRenderer;
    }


}
