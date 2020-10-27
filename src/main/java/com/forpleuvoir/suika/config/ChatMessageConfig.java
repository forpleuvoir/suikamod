package com.forpleuvoir.suika.config;


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
    private String prefix = "&d";
    private String append = "❀";
    public static transient String defPrefix = "&d";
    public static transient String defAppend = "❀";


    public ChatMessageConfig() {
        this.setDefault();
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
        this.prefix = defPrefix;
        this.append = defAppend;
    }

    @Override
    public String toString() {
        return "ChatMessageConfig{" +
                ", prefix='" + prefix + '\'' +
                ", append='" + append + '\'' +
                '}';
    }
}
