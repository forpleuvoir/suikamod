package com.forpleuvoir.suika.config;

import com.google.gson.JsonObject;


/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.config
 * @ClassName ChatMessageConfig
 * @CreateTime 2020/10/20 11:25
 * @Description 聊天信息注入
 */
public class ChatMessageConfig {
    public transient static final String KEY = "chat_message";
    private String prefix = "";
    private String append = "";

    /**
     * 加载配置
     *
     * @param data 数据
     */
    public void loadConfig(JsonObject data) {
        JsonObject jsonObject = data.get(KEY).getAsJsonObject();
        this.prefix = jsonObject.get("prefix").getAsString();
        this.append = jsonObject.get("append").getAsString();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public static String getKEY() {
        return KEY;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getAppend() {
        return append;
    }

    public void set(String prefix, String append) {
        this.prefix = prefix;
        this.append = append;
    }

    public void setAppend(String append) {
        this.append = append;
    }

    public void setDefault() {
        this.prefix = "&d";
        this.append = "❀";
    }

    @Override
    public String toString() {
        return "ChatMessageConfig{" +
                ", prefix='" + prefix + '\'' +
                ", append='" + append + '\'' +
                '}';
    }
}
