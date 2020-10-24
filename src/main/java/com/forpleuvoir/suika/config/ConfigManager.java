package com.forpleuvoir.suika.config;

import com.forpleuvoir.suika.util.FileUtil;
import com.google.gson.*;
import net.minecraft.client.MinecraftClient;

import java.io.File;

import static com.forpleuvoir.suika.Suika.LOGGER;

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
    private static final String FILE_DIR = MinecraftClient.getInstance().runDirectory + "/config/suika";
    private static final String CONFIG_FILE_NAME = "suika_config.json";
    private static final String DATA_FILE_NAME = "suika_data.json";
    private static File CONFIG_FILE;
    private static File DATA_FILE;
    public static boolean hit = true;
    public static boolean autoRebirth = false;
    public static ChatMessageConfig cmConfig;
    public static TooltipConfig ttConfig;
    private static JsonObject CONFIG = new JsonObject();
    private static JsonObject DATA = new JsonObject();
    public static final String TOOLTIP = "tt";
    public static final String CHAT_MESSAGE = "cm";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    static String str = "{\n" +
            "    \"item.minecraft.melon_slice\": {\n" +
            "      \"enable\": true,\n" +
            "      \"tips\": [\n" +
            "        \"§d这个西瓜片看起来很奇怪的样子...能吃吗...\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"block.minecraft.player_head:YuyukoSAMA\": {\n" +
            "      \"enable\": true,\n" +
            "      \"tips\": [\n" +
            "        \"§d超可爱的幽幽子\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"block.minecraft.player_head:forpleuvoir\": {\n" +
            "      \"enable\": true,\n" +
            "      \"tips\": [\n" +
            "        \"§6孤独传说\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"block.minecraft.player_head:dhwuia\": {\n" +
            "      \"enable\": true,\n" +
            "      \"tips\": [\n" +
            "        \"§bdhwuia的头,看起来就很憨批,却意外的可爱...\",\n" +
            "        \"§b是个很讨人喜欢的家伙呢,但是却消失很久了...\"\n" +
            "      ]\n" +
            "    }\n" +
            "  }";
    static String s = "{\n" +
            "  \"prefix\": \"&d\",\n" +
            "  \"append\": \"❀\"\n" +
            "}";

    static {
        cmConfig = new ChatMessageConfig(config -> {
            // TODO: 2020/10/20 实现属性更新回调
            changeConfig();
            saveConfig();
            return true;
        });
        ttConfig = new TooltipConfig(config -> {
            // TODO: 2020/10/20
            changeConfig();
            saveConfig();
            return true;
        });
    }

    public static void init() {
        loadConfig();
    }


    public static void loadConfig() {
        try {
            LOGGER.info("suika mod check config...");
            checkConfig();
            LOGGER.info("suika mod load file...");
            loadFile();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("suika mod file load fail...");
            LOGGER.info("suika mod create config file...");
            createConfigFile();
        }
        cmConfig.loadConfig(DATA, CONFIG);
        ttConfig.loadConfig(DATA, CONFIG);
    }

    /**
     * 创建配置文件
     */
    public static void createConfigFile() {
        try {
            DATA_FILE = FileUtil.createFile(FILE_DIR, DATA_FILE_NAME);
            CONFIG_FILE = FileUtil.createFile(FILE_DIR, CONFIG_FILE_NAME);
            DATA.add(TOOLTIP, new JsonParser().parse(str).getAsJsonObject());
            DATA.add(CHAT_MESSAGE, new JsonParser().parse(s).getAsJsonObject());
            saveConfig();
        } catch (Exception e) {
        }
    }

    public static void changeConfig() {
        JsonObject cm = new JsonObject();
        cm.addProperty("prefix", cmConfig.getPrefix());
        cm.addProperty("append", cmConfig.getAppend());
        DATA.add(CHAT_MESSAGE, cm);
        DATA.add(TOOLTIP, GSON.toJsonTree(ttConfig.getDatas()));
    }

    public static void saveConfig() {
        try {
            FileUtil.writeFile(DATA_FILE, GSON.toJson(DATA));
            FileUtil.writeFile(CONFIG_FILE, GSON.toJson(CONFIG));
        } catch (Exception e) {
            LOGGER.info("suika mod file save fail...");
            LOGGER.error(e.getMessage());
        }
    }

    public static void loadFile() throws Exception {
        DATA_FILE = new File(FILE_DIR, DATA_FILE_NAME);
        CONFIG_FILE = new File(FILE_DIR, CONFIG_FILE_NAME);
        DATA = new JsonParser().parse(FileUtil.loadFile(FILE_DIR, DATA_FILE_NAME)).getAsJsonObject();
        CONFIG = new JsonParser().parse(FileUtil.loadFile(FILE_DIR, CONFIG_FILE_NAME)).getAsJsonObject();
    }

    /**
     * 检查配置文件
     *
     * @throws Exception FileNotFoundException
     */
    public static void checkConfig() throws Exception {
        FileUtil.loadFile(FILE_DIR, DATA_FILE_NAME);
        FileUtil.loadFile(FILE_DIR, CONFIG_FILE_NAME);
    }

    public static JsonObject getConfigAsObject(SuikaConfig key) {
        return CONFIG.getAsJsonObject(key.getKey());
    }

    public static JsonElement getConfig(SuikaConfig key) {
        return CONFIG.get(key.getKey());
    }

    public static boolean getConfigAsBoolean(SuikaConfig key){
        return getConfig(key).getAsBoolean();
    }

    public static boolean setConfig(SuikaConfig config, Object value) {
        try {
            CONFIG.add(config.getKey(), GSON.toJsonTree(value, config.getType()));
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

}
