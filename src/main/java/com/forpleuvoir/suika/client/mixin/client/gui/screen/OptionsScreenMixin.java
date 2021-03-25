package com.forpleuvoir.suika.client.mixin.client.gui.screen;

import com.forpleuvoir.suika.client.gui.SuikaConfigScreen;
import com.forpleuvoir.suika.client.util.CommandUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * 原版设置注入
 *
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.client.mixin.client.gui.screen
 * @class_name OptionsScreen
 * @create_time 2020/10/27 15:25
 */
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    private OptionsScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        int suika_top = this.height / 6 + 42;
        ButtonWidget suika_button = new ButtonWidget(this.width / 2 + 5, suika_top + 96, 150, 20, new TranslatableText("title.suika.config"), button1 -> CommandUtil.openScreen(SuikaConfigScreen.initScreen(this)));
        this.addButton(suika_button);
    }

}
