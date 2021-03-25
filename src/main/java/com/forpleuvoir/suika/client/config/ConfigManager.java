package com.forpleuvoir.suika.client.config;

import com.forpleuvoir.suika.client.Suika;
import com.forpleuvoir.suika.client.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理
 *
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.client.config
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
            JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
            DATA.put(TooltipConfig.KEY, GSON.fromJson(jsonObject.get(TooltipConfig.KEY), TooltipConfig.class));
            DATA.put(ChatMessageFilter.KEY, GSON.fromJson(jsonObject.get(ChatMessageFilter.KEY), ChatMessageFilter.class));
            DATA.put(RemarkPlayer.KEY, GSON.fromJson(jsonObject.get(RemarkPlayer.KEY), RemarkPlayer.class));
            DATA.put(FastCommand.KEY, GSON.fromJson(jsonObject.get(FastCommand.KEY), FastCommand.class));
        } catch (Exception e) {
            Suika.LOGGER.error("suika mod 数据加载失败");
            Suika.LOGGER.error(e.getMessage());
            Suika.LOGGER.info("suika mod 加载默认数据");
            TooltipConfig tooltipConfig = new TooltipConfig();
            tooltipConfig.setDefault();
            ConfigManager.DATA.put(TooltipConfig.KEY, tooltipConfig);
            saveData();
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

    public static ChatMessageFilter getFilter() {
        ChatMessageFilter filter = (ChatMessageFilter) DATA.get(ChatMessageFilter.KEY);
        if (filter == null) {
            filter = new ChatMessageFilter();
            DATA.put(ChatMessageFilter.KEY, filter);
            saveData();
        }
        return filter;
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

    public static RemarkPlayer getRemark() {
        RemarkPlayer remarkPlayer = (RemarkPlayer) DATA.get(RemarkPlayer.KEY);
        if (remarkPlayer == null) {
            remarkPlayer = new RemarkPlayer();
            DATA.put(RemarkPlayer.KEY, remarkPlayer);
            saveData();
        }
        return remarkPlayer;
    }

    public static FastCommand getFastCommand() {
        FastCommand fastCommand = (FastCommand) DATA.get(FastCommand.KEY);
        if (fastCommand == null) {
            fastCommand = new FastCommand();
            DATA.put(FastCommand.KEY,fastCommand);
            saveData();
        }
        return fastCommand;
    }


}
