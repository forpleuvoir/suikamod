package com.forpleuvoir.suika.config;

import java.lang.reflect.Type;

/**
 * 配置枚举类
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.config
 * @className Config
 * @createTime 2020/10/24 17:37
 */
public enum SuikaConfig {
    /**
     *
     */
    CHAT_MESSAGE("chat_message", true,Boolean.TYPE),
    TOOLTIP("tooltip", true,Boolean.TYPE),
    AUTO_REBIRTH("auto_rebirth", false,Boolean.TYPE),
    SHOW_ENCHANTMENT("show_enchantment", true,Boolean.TYPE);

    private String key;
    private Object value;
    private Type type;

    SuikaConfig(String key, Object value, Type type) {
        this.key = key;
        this.value = value;
        this.type=type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
