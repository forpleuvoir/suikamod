package com.forpleuvoir.suika.client.compat.modmenu;

import com.forpleuvoir.suika.client.gui.SuikaConfigScreen;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

/**
 * mod menu接入
 *
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.client.compat.modmenu
 * @class_name ModMenuImpl
 * @create_time 2020/10/27 10:14
 */

public class ModMenuImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SuikaConfigScreen::initScreen;
    }
}
