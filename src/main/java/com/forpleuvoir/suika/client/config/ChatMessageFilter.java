package com.forpleuvoir.suika.client.config;

import com.forpleuvoir.suika.util.Callback;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * #package com.forpleuvoir.suika.client.config
 * #class_name ChatMessageFilter
 * #create_time 2021/2/28 16:52
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class ChatMessageFilter {

    public static transient String KEY = "chat_message_filter";
    private transient final Callback callback;

    public Set<Data> getDatas() {
        return datas;
    }

    private final Set<Data> datas = new HashSet<>();


    public ChatMessageFilter() {
        this.callback = ConfigManager::saveData;
    }

    private void onUpdate() {
        callback.callback();
    }

    public void add(String text, Type type) {
        if (!hasDatas(text)) {
            datas.add(new Data(text, type));
            onUpdate();
        } else {
            for (Data data : datas) {
                if (data.content.equals(text) && data.type != type) {
                    data.type = type;
                    onUpdate();
                }
            }
        }
    }

    public void add(String text) {
        add(text, Type.EQUALS);
    }

    public boolean remove(String text) {
        if (hasDatas(text)) {
            datas.removeIf(next -> next.content.equals(text));
            onUpdate();
            return true;
        } else {
            return false;
        }
    }

    public boolean filter(String text) {
        for (Data data : datas) {
            if (data.type == Type.EQUALS) {
                if (text.equals(data.content))
                    return true;
            } else if (data.type == Type.CONTAIN) {
                if (text.contains(data.content)) {
                    return true;
                }
            } else if (data.type == Type.REGEX) {
                if(Pattern.matches(data.content, text)){
                    return true;
                }
            }
        }
        return false;
    }

    public Data getData(String text) {
        for (Data data : datas) {
            if (data.content.equals(text)) {
                return data;
            }
        }
        return null;
    }

    public boolean hasDatas(String text) {
        for (Data data : datas) {
            if (data.content.equals(text)) {
                return true;
            }
        }
        return false;
    }


    public static class Data {
        public Type type;
        public String content;

        public Data(String content, Type type) {
            this.type = type;
            this.content = content;
        }

        public Data(String content) {
            this(content, Type.EQUALS);
        }
    }

    public enum Type {
        EQUALS("equals"),
        CONTAIN("contain"),
        REGEX("regex");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static Type byName(String name) {
            switch (name) {
                case "contain":
                    return CONTAIN;
                case "regex":
                    return REGEX;
                default:
                    return EQUALS;
            }
        }
    }
}
