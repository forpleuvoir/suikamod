package com.forpleuvoir.suika.client;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage kge com.forpleuvoir.suika
 * @ClassName Suika
 * @CreateTime 2020/10/19 11:43
 * @Description mod初始化类
 */
public class Suika implements ModInitializer {
    public static final String MOD_ID = "suika";
    public static final Logger LOGGER = LogManager.getLogger(Suika.MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("suika mod initialize...");
    }
}
