package com.forpleuvoir.suika.client;

import com.forpleuvoir.suika.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * @BelongsProject suikamod
 * @BelonsPackge com.forpleuvoir.suika.client
 * @ClassName SuikaClient
 * @author forpleuvoir
 * @CreateTime 2020/10/19 11:44
 * @Description 客户端初始化类
 */
@Environment(EnvType.CLIENT)
public class SuikaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.init();
    }
}
