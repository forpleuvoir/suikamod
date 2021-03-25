package com.forpleuvoir.suika.client.data;

import com.forpleuvoir.suika.client.SuikaClient;
import net.minecraft.client.MinecraftClient;

/**
 * #package com.forpleuvoir.suika.client.data
 * #class_name TimeTask
 * #create_time 2021/3/19 13:56
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class TimeTask {
    private int num;
    private final int time;//多少tick执行一次
    private int _time = 0;
    private final Type type;
    private final String content;

    public TimeTask(int num, int time, Type type, String content) {
        this.num = num;
        this.time = time;
        this.type = type;
        this.content = content;
        this._time = time;
    }

    public void run() {
        _time++;
        if (num != 0 && ((_time % time) == 0)) {
            this.run(SuikaClient.client);
            num--;
        }
    }


    private void run(MinecraftClient client) {
        switch (type) {
            case SEND_MESSAGE:
                SuikaClient.sendChatMessage(content);
                break;

        }
    }

    public boolean isRemoved() {
        return num == 0;
    }


    public enum Type {
        SEND_MESSAGE("send_message");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
