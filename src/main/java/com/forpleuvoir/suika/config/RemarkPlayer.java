package com.forpleuvoir.suika.config;

import com.forpleuvoir.suika.util.Callback;
import com.mojang.authlib.GameProfile;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * #package com.forpleuvoir.suika.config
 * #class_name RemarkPlayer
 * #create_time 2021/3/1 17:53
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class RemarkPlayer {
    public static final transient String KEY = "remark_player";
    private final transient Callback callback;
    private Map<String, String> datas = new HashMap<>();

    public RemarkPlayer() {
        this.callback = ConfigManager::saveData;
    }

    private void onUpdate() {
        callback.callback();
    }

    public Text build(Text text, UUID uuid) {
        if (datas.containsKey(uuid.toString())) {
            return new LiteralText(datas.get(uuid.toString())).append(text);
        }
        return text;
    }

    public boolean set(GameProfile player, String text) {
        if (text.contains("&")) {
            text = text.replace("&", "§");
        }
        try {
            datas.put(player.getId().toString(), text);
            onUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remove(GameProfile player) {
        if (player != null && datas.containsKey(player.getId().toString())) {
            datas.remove(player.getId().toString());
            onUpdate();
            return true;
        }
        return false;
    }

}
