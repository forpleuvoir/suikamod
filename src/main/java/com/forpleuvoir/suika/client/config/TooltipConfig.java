package com.forpleuvoir.suika.client.config;

import com.forpleuvoir.suika.client.util.Callback;
import com.forpleuvoir.suika.client.Suika;

import java.util.*;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.client.config
 * @ClassName TooltipConfig
 * @CreateTime 2020/10/20 13:06
 * @Description Tooltip配置
 */
public class TooltipConfig {
    public transient static final String KEY = "tooltip";
    private final Map<String, Data> datas = new HashMap<>();
    private transient final Callback callback;

    public TooltipConfig() {
        this.callback = ConfigManager::saveData;
    }

    public Map<String, Data> getDatas() {
        return datas;
    }

    /**
     * 设置默认数据
     */
    public void setDefault() {
        datas.put("item.minecraft.melon_slice",new Data("§d这个西瓜片看起来很奇怪的样子...能吃吗...","§6好怪的西瓜"));
        datas.put("block.minecraft.player_head:YuyukoSAMA",new Data("§d超可爱的幽幽子"));
        datas.put("block.minecraft.player_head:forpleuvoir",new Data("§6孤独传说"));
        datas.put("block.minecraft.player_head:dhwuia",new Data("§b某电姓人类的头,看起来就很憨批,却意外的可爱...","§b是个很讨人喜欢的家伙呢,但是却消失很久了..."));
        datas.put("item.suika.ibuki_gourd",new Data("§e里面的酒喝不完的样子..."));
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
    public void addData(String key, boolean enabled, String... tips) {
        Data data = new Data(tips);
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

    /**
     * 数据类
     */
    public static class Data {
        private boolean enable = true;
        private List<String> tips = new ArrayList<>();
        Data(){}

        Data(String... tips){
            Collections.addAll(this.tips, tips);
        }

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
