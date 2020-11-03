package com.forpleuvoir.suika.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串类型的配置
 *
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.config
 * @class_name StringConfig
 * @create_time 2020/11/3 17:04
 */

public class StringConfig {
    public transient static final String KEY = "string";
    private transient final Callback callback;
    private Map<String, String> datas = new HashMap<>();

    public StringConfig() {
        callback = ConfigManager::saveData;
    }

    public String get(String key) {
        return datas.get(key);
    }

    public void put(String key,String value){
        datas.put(key,value);
        onChanged();
    }


    private void onChanged(){
        callback.callback();
    }
}
