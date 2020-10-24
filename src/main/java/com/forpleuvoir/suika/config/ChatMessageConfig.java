package com.forpleuvoir.suika.config;

import com.google.gson.JsonObject;

import static com.forpleuvoir.suika.config.ConfigManager.CHAT_MESSAGE;

/**
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.config
 * @ClassName ChatMessageConfig
 * @author forpleuvoir
 * @CreateTime 2020/10/20 11:25
 * @Description 聊天信息注入
 */
public class ChatMessageConfig {
    private String prefix = "";
    private String append = "";
    private final Changed<ChatMessageConfig> changed;

    public ChatMessageConfig(Changed<ChatMessageConfig> changed) {
        this.changed = changed;
    }

    /**
     * 加载配置
     * @param data 数据
     * @param config 配置
     */
    public void loadConfig(JsonObject data, JsonObject config) {
        JsonObject jsonObject = data.get(CHAT_MESSAGE).getAsJsonObject();
        this.prefix = jsonObject.get("prefix").getAsString();
        this.append = jsonObject.get("append").getAsString();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean setPrefix(String prefix) {
        this.prefix = prefix;
        return this.onChanged();
    }

    public String getAppend() {
        return this.append;
    }

    public boolean setAppend(String append) {
        this.append = append;
        return this.onChanged();
    }

    public boolean onChanged() {
        return this.changed.onChanged(this);
    }

    @Override
    public String toString() {
        return "ChatMessageConfig{" +
                ", prefix='" + prefix + '\'' +
                ", append='" + append + '\'' +
                '}';
    }

    interface Changed<T> {
        /**
         * 属性发生变化时调用
         *
         * @param config 实例
         * @return 是否成功
         */
        boolean onChanged(T config);
    }
}
