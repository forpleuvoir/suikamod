package com.forpleuvoir.suika.config;

/**
 * 配置枚举类
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.config
 * @className Config
 * @createTime 2020/10/24 17:37
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum SuikaConfig {

    CHAT_MESSAGE("chat_message", true),
    TOOLTIP("tooltip", true),
    AUTO_REBIRTH("auto_rebirth", false),
    SHOW_ENCHANTMENT("show_enchantment", true),
    CHAT_BUBBLES("chat_bubbles", true),
    CUSTOM_CHAT_MESSAGE_VALUE("custom_chat_message_value", "/back"),
    CUSTOM_CHAT_MESSAGE_ENABLE("custom_chat_message", false);

    private final String key;
    private final Object value;

    /**
     * 配置枚举
     *
     * @param key   键值
     * @param value 默认值
     */
    SuikaConfig(String key, Object value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

}
