package com.forpleuvoir.suika.client.mixin.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

import static com.forpleuvoir.suika.client.config.ModConfigApp.MOD_CONFIG;
import static com.forpleuvoir.suika.client.util.TooltipUtil.addTooltip;

/**
 * 后面再说
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.client.mixin.client.gui.hud
 * @className InGameHudMixin
 * @createTime 2020/10/22 9:49
 */

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

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


//    public void renderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci) {
//        this.client.getProfiler().push("selectedItemName");
//        if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
//            MutableText mutableText = (new LiteralText("")).append(this.currentStack.getName()).formatted(this.currentStack.getRarity().formatting);
//            if (this.currentStack.hasCustomName()) {
//                mutableText.formatted(Formatting.ITALIC);
//            }
//            LinkedList<MutableText> list = new LinkedList<>();
//            list.addFirst(mutableText);
//            if (MOD_CONFIG.getShowEnchantment()) {
//                addTooltip(currentStack, list);
//                if (!currentStack.getEnchantments().isEmpty()) {
//                    for (int i = 0; i < currentStack.getEnchantments().size(); i++) {
//                        CompoundTag compoundTag = currentStack.getEnchantments().getCompound(i);
//                        int lvl = compoundTag.getInt("lvl");
//                        String str = compoundTag.getString("id");
//                        String key = "enchantment." + str.split(":")[0] + "." + str.split(":")[1];
//                        MutableText e = new TranslatableText(key).append(" ");
//                        if (lvl != 1) {
//                            e.append(new TranslatableText("enchantment.level." + lvl));
//                        }
//                        e.formatted(Formatting.DARK_AQUA);
//                        list.addLast(e);
//                    }
//                }
//            }
//            int i = this.getFontRenderer().getWidth(mutableText);
//            int j = (this.scaledWidth - i) / 2;
//            int k = this.scaledHeight - 59;
//            assert this.client.interactionManager != null;
//            if (!this.client.interactionManager.hasStatusBars()) {
//                k += 14;
//            }
//
//            int l = (int) ((float) this.heldItemTooltipFade * 256.0F / 10.0F);
//            if (l > 255) {
//                l = 255;
//            }
//
//            if (l > 0) {
//                RenderSystem.enableBlend();
//                RenderSystem.defaultBlendFunc();
//                int var10001 = j - 2;
//                int var10002 = k - 2;
//                int var10003 = j + i + 2;
//                this.getFontRenderer().getClass();
//                DrawableHelper.fill(matrices, var10001, var10002, var10003, k + 9 + 2, this.client.options.getTextBackgroundColor(0));
//                int count = 0;
//                int padding = 10;
//                int size = list.size();
//                int newK = k - ((size - 1) * padding);
//                for (MutableText e : list) {
//                    int a = this.getFontRenderer().getWidth(e);
//                    int b = (this.scaledWidth - a) / 2;
//                    this.getFontRenderer().drawWithShadow(matrices, e, (float) b, (float) newK + (count * padding), 16777215 + (l << 24));
//                    count++;
//                }
//                RenderSystem.disableBlend();
//                RenderSystem.popMatrix();
//            }
//        }
//        this.client.getProfiler().pop();
//        ci.cancel();
//    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    public void renderHeldItemTooltip(MatrixStack matrices,CallbackInfo ci) {
        this.client.getProfiler().push("selectedItemName");
        if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
            MutableText mutableText = (new LiteralText("")).append(this.currentStack.getName()).formatted(this.currentStack.getRarity().formatting);
            if (this.currentStack.hasCustomName()) {
                mutableText.formatted(Formatting.ITALIC);
            }
            LinkedList<MutableText> list = new LinkedList<>();
            list.addFirst(mutableText);
            if (MOD_CONFIG.getShowEnchantment()) {
                addTooltip(currentStack, list);
                if (!currentStack.getEnchantments().isEmpty()) {
                    for (int i = 0; i < currentStack.getEnchantments().size(); i++) {
                        NbtCompound compoundTag = currentStack.getEnchantments().getCompound(i);
                        int lvl = compoundTag.getInt("lvl");
                        String str = compoundTag.getString("id");
                        String key = "enchantment." + str.split(":")[0] + "." + str.split(":")[1];
                        MutableText e = new TranslatableText(key).append(" ");
                        if (lvl != 1) {
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
                    int a = this.getFontRenderer().getWidth(e);
                    int b = (this.scaledWidth - a) / 2;
                    this.getFontRenderer().drawWithShadow(matrices, e, (float) b, (float) newK + (count * padding), 16777215 + (l << 24));
                    count++;
                }
//                this.getFontRenderer().drawWithShadow(matrices, (Text) mutableText, (float) j, (float) k, 16777215 + (l << 24));
                RenderSystem.disableBlend();
            }
        }

        this.client.getProfiler().pop();
        ci.cancel();
    }

    public TextRenderer getFontRenderer() {
        return this.client.textRenderer;
    }


}
