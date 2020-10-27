package com.forpleuvoir.suika.config;

import com.forpleuvoir.suika.Suika;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.config
 * @ClassName TooltipConfig
 * @CreateTime 2020/10/20 13:06
 * @Description Tooltip配置
 */
public class TooltipConfig {
    public transient static final String KEY = "tooltip";
    private Map<String, Data> datas = new HashMap<>();
    private transient final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private transient final Callback callback;
    private final transient static String DEFAULT_DATA = "{\n" +
            "      \"item.minecraft.melon_slice\": {\n" +
            "        \"enable\": true,\n" +
            "        \"tips\": [\n" +
            "          \"§d这个西瓜片看起来很奇怪的样子...能吃吗...\",\n" +
            "          \"§6好怪的西瓜\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"block.minecraft.player_head:YuyukoSAMA\": {\n" +
            "        \"enable\": true,\n" +
            "        \"tips\": [\n" +
            "          \"§d超可爱的幽幽子\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"block.minecraft.player_head:forpleuvoir\": {\n" +
            "        \"enable\": true,\n" +
            "        \"tips\": [\n" +
            "          \"§6孤独传说\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"block.minecraft.player_head:dhwuia\": {\n" +
            "        \"enable\": true,\n" +
            "        \"tips\": [\n" +
            "          \"§bdhwuia的头,看起来就很憨批,却意外的可爱...\",\n" +
            "          \"§b是个很讨人喜欢的家伙呢,但是却消失很久了...\"\n" +
            "        ]\n" +
            "      },\n" +
            "      \"item.suika.ibuki_gourd\": {\n" +
            "        \"enable\": true,\n" +
            "        \"tips\": [\n" +
            "          \"§e里面的酒喝不完的样子...\"\n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  }";

    public TooltipConfig() {
        this.callback = ConfigManager::saveData;
    }

    public Map<String, Data> getDatas() {
        return datas;
    }

    public void setDefault() {
        this.datas = GSON.fromJson(new JsonParser().parse(DEFAULT_DATA), new TypeToken<Map<String, Data>>() {
        }.getType());
    }

    /**
     * 通过键值 获取数据
     *
     * @param key 键值
     * @return {@link Data}
     */
    public Data getData(String key) {
        return this.datas.get(key);
    }

    /**
     * 删除数据
     *
     * @param key   键值
     * @param index 数据下标 1开始数 0则是全部删除
     */
    public void remove(String key, int index) {
        if (datas.containsKey(key)) {
            if (index <= -1) {
                datas.remove(key);
                onChanged();
            }
            try {
                if (index >= 0) {
                    datas.get(key).removeTip(index);
                    onChanged();
                }
            } catch (Exception e) {
                Suika.LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * 设置启用状态
     *
     * @param key    键值
     * @param enable 是否启用
     */
    public void setEnable(String key, boolean enable) {
        Data data;
        if (datas.containsKey(key)) {
            data = datas.get(key);
            data.setEnable(enable);
            onChanged();
        }
    }

    /**
     * 批量添加数据
     *
     * @param key     键值
     * @param tips    数据列表
     * @param enabled 是否启用
     */
    public void addData(String key, List<String> tips, boolean enabled) {
        Data data = new Data();
        data.setTips(tips);
        data.setEnable(enabled);
        datas.put(key, data);
        onChanged();
    }

    /**
     * 添加数据
     *
     * @param key     键值
     * @param tip     数据
     * @param enabled 是否启用
     */
    public void addData(String key, String tip, boolean enabled) {
        Data data;
        if (datas.containsKey(key)) {
            data = datas.get(key);
            data.setEnable(enabled);
            data.addTip(tip);
            return;
        }
        data = new Data();
        data.addTip(tip);
        data.setEnable(enabled);
        datas.put(key, data);
        onChanged();
    }

    private void onChanged() {
        callback.callback();
    }

    public interface Callback {
        /**
         * 回调方法
         */
        void callback();
    }

    /**
     * 数据类
     */
    public static class Data {
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
