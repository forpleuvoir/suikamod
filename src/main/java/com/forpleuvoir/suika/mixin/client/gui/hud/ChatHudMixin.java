package com.forpleuvoir.suika.mixin.client.gui.hud;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;

/**
 * #package com.forpleuvoir.suika.mixin.client.gui.hud
 * #class_name ChatHudMixin
 * #create_time 2021/3/1 20:07
 * #project_name suikamod
 *
 * @author forpleuvoir
 */

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

//    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V",at =@At("HEAD"),argsOnly = true)
//    private Text addRemark(Text text){
//        if(modConfig.getRemarkPlayer()){
//            Text text1 = ConfigManager.getRemark().build(text, senderUuid);
//
//        }
//        return text;
//    }
}
