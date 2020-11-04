package com.forpleuvoir.suika.config;

import com.forpleuvoir.suika.Suika;
import com.forpleuvoir.suika.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理
 *
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.config
 * @ClassName ConfigManager
 * @CreateTime 2020/10/20 11:16
 */
public class ConfigManager {
    private static final URL TEST_DIR = ConfigManager.class.getResource("/");
    private static final File MAIN_DIR = MinecraftClient.getInstance().runDirectory;
    private static final String FILE_DIR = MAIN_DIR + "/config/suika";
    private static final String CONFIG_FILE_NAME = "suika_config.json";
    private static final String DATA_FILE_NAME = "suika_data.json";
    private static File CONFIG_FILE;
    private static File DATA_FILE;
    public static Map<String, Object> CONFIG = new HashMap<>();
    public static Map<String, Object> DATA = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        loadFiles();
        loadConfig();
        loadData();
    }


    public static void loadFiles() {
        Suika.LOGGER.info("suika mod load file...");
        CONFIG_FILE = new File(FILE_DIR, CONFIG_FILE_NAME);
        DATA_FILE = new File(FILE_DIR, DATA_FILE_NAME);
    }



    public static void loadConfig() {
        try {
            Suika.LOGGER.info("suika mod load config...");
            String config = FileUtil.readFile(CONFIG_FILE);
            Map<String, Object> map = GSON.fromJson(new JsonParser().parse(config).getAsJsonObject(), new TypeToken<Map<String, Object>>() {
            }.getType());
            map.forEach((k, v) -> {
                ConfigManager.CONFIG.put(k, v);
            });
        } catch (Exception e) {
            Suika.LOGGER.error("suika mod 配置加载失败");
            Suika.LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadData() {
        try {
            Suika.LOGGER.info("suika mod load data...");
            String data = FileUtil.readFile(DATA_FILE);
            ConfigManager.DATA.put(TooltipConfig.KEY, GSON.fromJson(new JsonParser().parse(data).getAsJsonObject().get(TooltipConfig.KEY), TooltipConfig.class));
            ConfigManager.DATA.put(ChatMessageConfig.KEY, GSON.fromJson(new JsonParser().parse(data).getAsJsonObject().get(ChatMessageConfig.KEY), ChatMessageConfig.class));
        } catch (Exception e) {
            Suika.LOGGER.error("suika mod 数据加载失败");
            Suika.LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        String config = GSON.toJson(CONFIG);
        try {
            FileUtil.writeFile(CONFIG_FILE, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        String data = GSON.toJson(DATA);
        try {
            FileUtil.writeFile(DATA_FILE, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T getConfig(SuikaConfig key, Class<T> clazz) {
        T t = clazz.cast(CONFIG.get(key.getKey()));
        if (t != null) {
            return t;
        } else {
            CONFIG.put(key.getKey(), key.getValue());
            saveConfig();
            return clazz.cast(key.getValue());
        }
    }

    public static <T> void setConfig(SuikaConfig key, T t) {
        CONFIG.put(key.getKey(), t.getClass().cast(t));
        saveConfig();
    }

    public static TooltipConfig.Data getTooltipData(String key) {
        TooltipConfig tooltipConfig = (TooltipConfig) DATA.get(TooltipConfig.KEY);
        if (tooltipConfig == null) {
            tooltipConfig = new TooltipConfig();
            tooltipConfig.setDefault();
            DATA.put(TooltipConfig.KEY, tooltipConfig);
            saveData();
        }
        return tooltipConfig.getData(key);
    }

    public static TooltipConfig getTooltip() {
        TooltipConfig tooltipConfig = (TooltipConfig) DATA.get(TooltipConfig.KEY);
        if (tooltipConfig == null) {
            tooltipConfig = new TooltipConfig();
            tooltipConfig.setDefault();
            DATA.put(TooltipConfig.KEY, tooltipConfig);
            saveData();
        }
        return tooltipConfig;
    }

    public static String getString(SuikaConfig config) {
        StringConfig stringConfig = (StringConfig) DATA.get(StringConfig.KEY);
        if (stringConfig == null) {
            stringConfig = new StringConfig();
            stringConfig.put(config.getKey(), (String) config.getValue());
            DATA.put(StringConfig.KEY, stringConfig);
            saveData();
        }
        return stringConfig.get(config.getKey());
    }

    public static void setString(SuikaConfig config, String value) {
        StringConfig stringConfig = (StringConfig) DATA.get(StringConfig.KEY);
        if (stringConfig == null) {
            stringConfig = new StringConfig();
            stringConfig.put(config.getKey(), value);
            DATA.put(StringConfig.KEY, stringConfig);
            saveData();
        }
        stringConfig.put(config.getKey(), value);
    }

    public static String[] getChatMessage() {
        ChatMessageConfig chatMessageConfig = (ChatMessageConfig) DATA.get(ChatMessageConfig.KEY);
        if (chatMessageConfig == null) {
            chatMessageConfig = new ChatMessageConfig();
            DATA.put(ChatMessageConfig.KEY, chatMessageConfig);
            saveData();
        }
        return new String[]{chatMessageConfig.getPrefix(), chatMessageConfig.getAppend()};
    }

    public static void setChatMessage(String prefix, String append) {
        ChatMessageConfig chatMessageConfig = (ChatMessageConfig) DATA.get(ChatMessageConfig.KEY);
        if (chatMessageConfig == null) {
            chatMessageConfig = new ChatMessageConfig();
        }
        chatMessageConfig.set(prefix, append);
        saveData();
    }

    public static void setChatMessagePrefix(String prefix) {
        ChatMessageConfig chatMessageConfig = (ChatMessageConfig) DATA.get(ChatMessageConfig.KEY);
        if (chatMessageConfig == null) {
            chatMessageConfig = new ChatMessageConfig();
        }
        chatMessageConfig.setPrefix(prefix);
        saveData();
    }

    public static void setChatMessageAppend(String append) {
        ChatMessageConfig chatMessageConfig = (ChatMessageConfig) DATA.get(ChatMessageConfig.KEY);
        if (chatMessageConfig == null) {
            chatMessageConfig = new ChatMessageConfig();
        }
        chatMessageConfig.setAppend(append);
        saveData();
    }

}
