package com.forpleuvoir.suika.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.forpleuvoir.suika.config.ConfigManager.TOOLTIP;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.config
 * @ClassName TooltipConfig
 * @CreateTime 2020/10/20 13:06
 * @Description Tooltip配置
 */
public class TooltipConfig {
    private Map<String, Data> datas = new HashMap<>();
    private transient final ChatMessageConfig.Changed<TooltipConfig> changed;


    public void loadConfig(JsonObject data, JsonObject config) {
        JsonObject jsonObject = data.get(TOOLTIP).getAsJsonObject();
        Gson gson = new Gson();
        this.datas = gson.fromJson(jsonObject, new TypeToken<Map<String, Data>>() {
        }.getType());
    }

    public TooltipConfig(ChatMessageConfig.Changed<TooltipConfig> changed) {
        this.changed = changed;
    }


    public Map<String, Data> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Data> datas) {
        this.datas = datas;
        this.changed.onChanged(this);
    }

    public Data getData(String key) {
        return this.datas.get(key);
    }

    public void remove(String key, int index) {
        if (datas.containsKey(key)) {
            if (index <= -1) {
                datas.remove(key);
            }
            try {
                if (index >= 0) {
                    datas.get(key).removeTip(index);
                }
            } catch (Exception e) {
            }
            this.changed.onChanged(this);
        }
    }

    public void setEnable(String key, boolean enable) {
        Data data;
        if (datas.containsKey(key)) {
            data = datas.get(key);
            data.setEnable(enable);
            this.changed.onChanged(this);
        }
    }


    public void addData(String key, List<String> tips, boolean enabled) {
        Data data = new Data();
        data.setTips(tips);
        data.setEnable(enabled);
        this.datas.put(key, data);
        this.changed.onChanged(this);
    }

    public void addData(String key, String tip, boolean enabled) {
        Data data;
        if (datas.containsKey(key)) {
            data = datas.get(key);
            data.setEnable(enabled);
            data.addTip(tip);
            this.changed.onChanged(this);
            return;
        }
        data = new Data();
        data.addTip(tip);
        data.setEnable(enabled);
        this.datas.put(key, data);
        this.changed.onChanged(this);
    }


    public class Data {
        private boolean enable = true;
        private List<String> tips = new ArrayList<>();

        public List<String> getTips() {
            return tips;
        }

        public void setTips(List<String> tips) {
            this.tips = tips;
        }

        public void addTip(String tip) {
            this.tips.add(tip);
        }

        public void removeTip(int index) {
            this.tips.remove(index);
        }

        public String getTip(int index) {
            return this.tips.get(index);
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
