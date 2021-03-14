package com.forpleuvoir.suika.client.config;


import com.forpleuvoir.suikalib.config.FieldType;
import com.forpleuvoir.suikalib.config.annotation.Config;
import com.forpleuvoir.suikalib.config.annotation.ConfigField;
import com.forpleuvoir.suikalib.config.annotation.ConfigUpdateCallback;
import com.forpleuvoir.suikalib.config.configInterface.UpdateCallback;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.client.config
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
    @ConfigField(value = "custom_chat_message_value",defValue = "/back",type = FieldType.STRING)
    private String customChatMessageValue;
    @ConfigField(value = "custom_chat_message",defValue = "true",type = FieldType.BOOLEAN)
    private Boolean customChatMessage;
    @ConfigField(value = "chat_message_prefix",defValue = "",type = FieldType.STRING)
    private String chatMessagePrefix;
    @ConfigField(value = "chat_message_append",defValue = "",type = FieldType.STRING)
    private String chatMessageAppend;
    @ConfigField(value = "remark_player",defValue = "true",type = FieldType.BOOLEAN)
    private Boolean remarkPlayer;
    @ConfigField(value = "debug",defValue = "false",type = FieldType.BOOLEAN)
    private Boolean debug;
    @ConfigField(value = "fast_command_gui",defValue = "false",type = FieldType.BOOLEAN)
    private Boolean fastCommandGui;

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

    public Boolean getRemarkPlayer() {
        return remarkPlayer;
    }

    public void setRemarkPlayer(Boolean remarkPlayer) {
        this.remarkPlayer = remarkPlayer;
        update();
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
        update();
    }

    public Boolean getFastCommandGui() {
        return fastCommandGui;
    }

    public void setFastCommandGui(Boolean fastCommandGui) {
        this.fastCommandGui = fastCommandGui;
        update();
    }
}
