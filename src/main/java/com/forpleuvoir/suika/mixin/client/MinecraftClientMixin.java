package com.forpleuvoir.suika.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.tutorial.TutorialManager;
import net.minecraft.client.tutorial.TutorialStep;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 客户端注入
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.mixin.client
 * @className MinecraftClientMixin
 * @createTime 2020/10/23 20:35
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Final
    private TutorialManager tutorialManager;

    @Inject(method = "<init>", at = @At("RETURN"), cancellable = true)
    public void init(CallbackInfo ci) {
        tutorialManager.setStep(TutorialStep.NONE);
    }

}
