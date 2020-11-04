package com.forpleuvoir.suika.client.gui;

import com.forpleuvoir.suika.config.ChatMessageConfig;
import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.config.SuikaConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

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
        chatMessage(builder);
        return builder.build();
    }

    private static void addStringConfig(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.suika.string"));
        for (SuikaConfig e : SuikaConfig.values()) {
            if (e.getValue() instanceof String) {
                general.addEntry(entryBuilder.startStrField(new TranslatableText("string.suika." + e.getKey()), ConfigManager.getString(e))
                        .setDefaultValue((String) e.getValue())
                        .setSaveConsumer(value -> ConfigManager.setString(e, value))
                        .build());
            }
        }
    }

    private static void addBooleanConfig(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.suika.switch"));
        for (SuikaConfig config : SuikaConfig.values()) {
            if (config.getValue() instanceof Boolean) {
                general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("boolean.suika." + config.getKey()), ConfigManager.getConfig(config, Boolean.class))
                        .setDefaultValue((Boolean) config.getValue())
                        .setSaveConsumer(newValue -> ConfigManager.setConfig(config, newValue))
                        .build());

            }
        }
    }

    private static void chatMessage(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.suika.chat_message"));
        general.addEntry(entryBuilder.startStrField(new TranslatableText("entry.suika.chat_message.prefix"), ConfigManager.getChatMessage()[0])
                .setDefaultValue(ChatMessageConfig.defPrefix)
                .setSaveConsumer(ConfigManager::setChatMessagePrefix).build());
        general.addEntry(entryBuilder.startStrField(new TranslatableText("entry.suika.chat_message.append"), ConfigManager.getChatMessage()[1])
                .setDefaultValue(ChatMessageConfig.defAppend)
                .setSaveConsumer(ConfigManager::setChatMessageAppend).build());
    }
}
