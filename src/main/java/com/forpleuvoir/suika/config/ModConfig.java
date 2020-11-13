package com.forpleuvoir.suika.config;


import com.forpleuvoir.suikalib.config.FieldType;
import com.forpleuvoir.suikalib.config.annotation.Config;
import com.forpleuvoir.suikalib.config.annotation.ConfigField;
import com.forpleuvoir.suikalib.config.annotation.ConfigUpdateCallback;
import com.forpleuvoir.suikalib.config.configInterface.UpdateCallback;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.config
 * @class_name ModConfig
 * @create_time 2020/11/11 18:44
 */
@Config("suika_mod_config")
public class ModConfig {

    @ConfigUpdateCallback
    private UpdateCallback updateCallback;

    @ConfigField(value = "chat_message",defValue = "false",type = FieldType.BOOLEAN)
    private Boolean chatMessage;
    @ConfigField(value = "tooltip",defValue = "true",type = FieldType.BOOLEAN)
    private Boolean tooltip;
    @ConfigField(value = "auto_rebirth",defValue = "false",type = FieldType.BOOLEAN)
    private Boolean AutoRebirth;
    @ConfigField(value = "show_enchantment",defValue = "true",type = FieldType.BOOLEAN)
    private Boolean showEnchantment;
    @ConfigField(value = "chat_bubbles",defValue = "true",type = FieldType.BOOLEAN)
    private Boolean chatBubbles;
    @ConfigField(value = "custom_chat_message_value",defValue = "/back",type = FieldType.STRING)
    private String customChatMessageValue;
    @ConfigField(value = "custom_chat_message",defValue = "true",type = FieldType.BOOLEAN)
    private Boolean customChatMessage;
    @ConfigField(value = "chat_message_prefix",defValue = "",type = FieldType.STRING)
    private String chatMessagePrefix;
    @ConfigField(value = "chat_message_append",defValue = "",type = FieldType.STRING)
    private String chatMessageAppend;

    public String getChatMessagePrefix() {
        return chatMessagePrefix;
    }

    public void setChatMessagePrefix(String chatMessagePrefix) {
        this.chatMessagePrefix = chatMessagePrefix;
        update();
    }

    public String getChatMessageAppend() {
        return chatMessageAppend;
    }

    public void setChatMessageAppend(String chatMessageAppend) {
        this.chatMessageAppend = chatMessageAppend;
        update();
    }

    public void update(){
        updateCallback.onUpdate(this);
    }

    public Boolean getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(Boolean chatMessage) {
        this.chatMessage = chatMessage;
        update();
    }

    public Boolean getTooltip() {
        return tooltip;
    }

    public void setTooltip(Boolean tooltip) {
        this.tooltip = tooltip;
        update();
    }

    public Boolean getAutoRebirth() {
        return AutoRebirth;
    }

    public void setAutoRebirth(Boolean autoRebirth) {
        AutoRebirth = autoRebirth;
        update();
    }

    public Boolean getShowEnchantment() {
        return showEnchantment;
    }

    public void setShowEnchantment(Boolean showEnchantment) {
        this.showEnchantment = showEnchantment;
        update();
    }

    public Boolean getChatBubbles() {
        return chatBubbles;
    }

    public void setChatBubbles(Boolean chatBubbles) {
        this.chatBubbles = chatBubbles;
        update();
    }

    public String getCustomChatMessageValue() {
        return customChatMessageValue;
    }

    public void setCustomChatMessageValue(String customChatMessageValue) {
        this.customChatMessageValue = customChatMessageValue;
        update();
    }

    public Boolean getCustomChatMessage() {
        return customChatMessage;
    }

    public void setCustomChatMessage(Boolean customChatMessage) {
        this.customChatMessage = customChatMessage;
        update();
    }
}
