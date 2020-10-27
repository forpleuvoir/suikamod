package com.forpleuvoir.chatbubbles;

/**
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.chatbubbles
 * @className ChatBubbleMessage
 * @createTime 2020/10/25 11:47
 */
public class ChatBubbleMessage {
    private final int updateCounterCreated;
    private final String author;
    private final String[] messageLines;

    public ChatBubbleMessage(String author, String[] messageLines, int created) {
        this.author = author;
        this.messageLines = messageLines;
        this.updateCounterCreated = created;
    }

    public String getAuthor() {
        return this.author;
    }

    public String[] getMessageLines() {
        return this.messageLines;
    }

    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }
}
