package com.forpleuvoir.suika.config;

import com.forpleuvoir.suika.util.Callback;
import com.forpleuvoir.suika.util.Log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * #package com.forpleuvoir.suika.config
 * #class_name FastCommand
 * #create_time 2021/3/6 14:46
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class FastCommand {
    private transient final Log log = new Log(FastCommand.class);
    private final transient Callback callback;
    public static transient String KEY = "fast_command";
    private final Map<String, String> datas = new HashMap<>();

    public FastCommand() {
        this.callback = ConfigManager::saveData;
    }

    private void update() {
        this.callback.callback();
    }

    public Set<String> getKeys(){
        return datas.keySet();
    }

    public void add(String key, String value) {
        log.info("添加快捷指令(处理前)", "key:" + key, "value:" + value);
        String val = value.startsWith("/") ? value : "/" + value;
        String k = key.contains("&") ? key.replace("&", "§") : key;
        log.info("添加快捷指令(处理后)", "key:" + k, "value:" + val);
        datas.put(k, val);
        update();
    }

    public boolean delete(String key){
        String k = key.contains("&") ? key.replace("&", "§") : key;
        if (datas.containsKey(k)) {
            datas.remove(k);
            update();
            return true;
        }else{
            return false;
        }

    }

    public void show() {
        if(!datas.isEmpty()) {
            message("快捷指令列表");
            MutableText text = new LiteralText("");
            Iterator<Map.Entry<String, String>> iterator = datas.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String val = entry.getValue();
                text.append(Texts.bracketed(new LiteralText(key)
                                .styled(style ->
                                        style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, val))
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText(val)))
                                )
                        )
                );
                if (iterator.hasNext())
                    text.append(new LiteralText(",  "));
            }
            message(text);
        }else {
            message("你没有添加任何指令");
        }
    }

    private void message(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }

    private void message(String text) {
        message(new LiteralText(text));
    }

}
