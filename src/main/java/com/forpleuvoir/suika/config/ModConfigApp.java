package com.forpleuvoir.suika.config;

import com.forpleuvoir.suikalib.config.SuikaConfigApp;
import com.forpleuvoir.suikalib.config.annotation.Config;
import com.forpleuvoir.suikalib.config.annotation.SuikaConfig;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.config
 * @class_name ModConfigApp
 * @create_time 2020/11/11 18:53
 */
@SuikaConfig(value = "suika", packages = "com.forpleuvoir.suika.config")
public class ModConfigApp {
    public static final File MAIN_DIR = MinecraftClient.getInstance().runDirectory;
    public static final String FILE_DIR = MAIN_DIR + "/config/suika/";

    @Config("suika_mod_config")
    public static ModConfig modConfig;

    public static void init() {
        File file =new File(FILE_DIR+"suikamod_config.json");
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ModConfig.class);
        SuikaConfigApp.init(ModConfigApp.class, classes,file);
    }
}
