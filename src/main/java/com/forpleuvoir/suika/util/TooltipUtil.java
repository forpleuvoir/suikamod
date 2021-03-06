package com.forpleuvoir.suika.util;

import com.forpleuvoir.suika.config.ConfigManager;
import com.forpleuvoir.suika.config.TooltipConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.Map;

import static com.forpleuvoir.suika.config.ModConfigApp.MOD_CONFIG;
import static com.forpleuvoir.suika.util.PlayerHeadUtil.getSkullOwner;
/**
 * tooltip工具类
 *
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.suika.util
 * @className TooltipUtil
 * @createTime 2020/10/22 11:35
 */
public class TooltipUtil {
    public static void addTooltip(ItemStack stack, List list) {
        if (MOD_CONFIG.getTooltip()) {
            String key = getKey(stack);
            Map<String, TooltipConfig.Data> map = ConfigManager.getTooltip().getDatas();
            if (map.containsKey(key)) {
                TooltipConfig.Data data = ConfigManager.getTooltip().getData(key);
                if (data.isEnable()) {
                    data.getTips().forEach(e -> list.add(new LiteralText(e)));
                }
            }
        }
    }

    public static String getKey(ItemStack stack) {
        String value = "";
        Item item = stack.getItem();
        String name = item.getName(stack).getString();
        String cName = stack.getName().getString();
        if (stack.getItem().equals(Items.PLAYER_HEAD)) {
            value = ":" + getSkullOwner(stack);
        } else if (!name.equals(cName)) {
            value = "#" + cName;
        }
        return item.getTranslationKey(stack) + value;
    }


}
