package com.forpleuvoir.suika.client.gui;

import com.forpleuvoir.suikalib.config.FieldType;
import com.forpleuvoir.suikalib.config.annotation.ConfigField;
import com.forpleuvoir.suikalib.reflection.ReflectionUtil;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

import java.lang.reflect.Field;
import java.util.List;

import static com.forpleuvoir.suika.config.ModConfigApp.modConfig;

/**
 * 配置gui
 *
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.client.gui
 * @class_name ConfigGui
 * @create_time 2020/10/27 14:14
 */
@Environment(EnvType.CLIENT)
public class SuikaConfigScreen {

    public static Screen initScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslatableText("title.suika.config"));
        if (parent != null)
            builder.setParentScreen(parent);
        builder.setTransparentBackground(true);
        addBooleanConfig(builder);
        addStringConfig(builder);
     //   chatMessage(builder);
        return builder.build();
    }

    private static void addStringConfig(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.suika.string"));
        List<Field> list = ReflectionUtil.getFieldByAnnotation(modConfig.getClass(), ConfigField.class);
        list.forEach(e -> {
            if (e.getType().equals(String.class)) {
                e.setAccessible(true);
                ConfigField configField = e.getAnnotation(ConfigField.class);
                String value = "";
                try {
                    value = (String) e.get(modConfig);
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
                String defaultValue = (String) FieldType.getDefValue(configField);
                general.addEntry(entryBuilder.startStrField(new TranslatableText("string.suika." + configField.value()), value)
                        .setDefaultValue(defaultValue)
                        .setSaveConsumer(v -> {
                            try {
                                e.set(modConfig,v);
                                modConfig.update();
                            } catch (IllegalAccessException illegalAccessException) {
                                illegalAccessException.printStackTrace();
                            }
                        }).build());
            }
        });
//        for (SuikaConfig e : SuikaConfig.values()) {
//            if (e.getValue() instanceof String) {
//                general.addEntry(entryBuilder.startStrField(new TranslatableText("string.suika." + e.getKey()), ConfigManager.getString(e))
//                        .setDefaultValue((String) e.getValue())
//                        .setSaveConsumer(value -> ConfigManager.setString(e, value))
//                        .build());
//            }
//        }
    }

    private static void addBooleanConfig(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.suika.switch"));
        List<Field> list = ReflectionUtil.getFieldByAnnotation(modConfig.getClass(), ConfigField.class);
        list.forEach(e -> {
            if (e.getType().equals(Boolean.class)) {
                e.setAccessible(true);
                ConfigField configField = e.getAnnotation(ConfigField.class);
                Boolean value = false;
                try {
                    value = (Boolean) e.get(modConfig);
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
                Boolean defaultValue = (Boolean) FieldType.getDefValue(configField);
                general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("boolean.suika." + configField.value()), value)
                        .setDefaultValue(defaultValue)
                        .setSaveConsumer(v -> {
                            try {
                                e.set(modConfig,v);
                                modConfig.update();
                            } catch (IllegalAccessException illegalAccessException) {
                                illegalAccessException.printStackTrace();
                            }
                        }).build());
            }
        });
//        for (SuikaConfig config : SuikaConfig.values()) {
//            if (config.getValue() instanceof Boolean) {
//                general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("boolean.suika." + config.getKey()), ConfigManager.getConfig(config, Boolean.class))
//                        .setDefaultValue((Boolean) config.getValue())
//                        .setSaveConsumer(newValue -> ConfigManager.setConfig(config, newValue))
//                        .build());
//
//            }
//        }
    }

}
