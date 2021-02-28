package com.forpleuvoir.suika.config;

import com.forpleuvoir.suika.Suika;
import com.forpleuvoir.suika.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    public static final String FILE_DIR = MAIN_DIR + "/config/suika";
    private static final String DATA_FILE_NAME = "suika_data.json";
    private static File DATA_FILE;
    public static Map<String, Object> DATA = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CHAT_MESSAGE_FILTER_KEY = "chat_message_filter";

    public static void init() {
        loadFiles();
        loadData();
    }


    public static void loadFiles() {
        Suika.LOGGER.info("suika mod load file...");
        DATA_FILE = new File(FILE_DIR, DATA_FILE_NAME);
        if (!DATA_FILE.exists()) {
            try {
                com.forpleuvoir.suikalib.util.FileUtil.createFile(DATA_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void loadData() {
        try {
            Suika.LOGGER.info("suika mod load data...");
            String data = FileUtil.readFile(DATA_FILE);
            DATA.put(TooltipConfig.KEY, GSON.fromJson(new JsonParser().parse(data).getAsJsonObject().get(TooltipConfig.KEY), TooltipConfig.class));
            DATA.put(CHAT_MESSAGE_FILTER_KEY, GSON.fromJson(new JsonParser().parse(data).getAsJsonObject().get(CHAT_MESSAGE_FILTER_KEY), Set.class));
        } catch (Exception e) {
            Suika.LOGGER.error("suika mod 数据加载失败");
            Suika.LOGGER.error(e.getMessage());
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

    public static Set<String> getFilter() {
        Set<String> filter = (Set<String>) DATA.get(CHAT_MESSAGE_FILTER_KEY);
        if (filter == null) {
            filter = new HashSet<>();
            DATA.put(CHAT_MESSAGE_FILTER_KEY, filter);
            saveData();
        }
        return filter;
    }

    public static void addFilter(String... text) {
        Collections.addAll(getFilter(), text);
        saveData();
    }

    public static boolean removeFilter(String text) {
        if(getFilter().contains(text)) {
            getFilter().remove(text);
            saveData();
            return true;
        }
        return false;
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

}
