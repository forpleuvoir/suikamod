package com.forpleuvoir.suika.client.util;

import static com.forpleuvoir.suika.client.SuikaClient.LOGGER;
import static com.forpleuvoir.suika.client.config.ModConfigApp.MOD_CONFIG;

/**
 * #package com.forpleuvoir.suika.util
 * #class_name Log
 * #create_time 2021/3/5 22:33
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class Log {

    private final Class<?> clazz;

    public Log(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void info(String message) {
        if (MOD_CONFIG != null) {
            if (MOD_CONFIG.getDebug()) {
                LOGGER.info(str(message));
            }
        } else {
            LOGGER.info(str(message));
        }
    }

    public void info(String... message) {
        if (MOD_CONFIG != null) {
            if (MOD_CONFIG.getDebug()) {
                for (String s : message) {
                    LOGGER.info(str(s));
                }
            }
        } else {
            for (String s : message) {
                LOGGER.info(str(s));
            }
        }
    }

    private String str(String... str) {
        StringBuilder string = new StringBuilder("suika debug[" + clazz.getName() + "]:");
        for (String s : str) {
            string.append(s);
        }
        return string.toString();
    }


}
