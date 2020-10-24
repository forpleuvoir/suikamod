package com.forpleuvoir.suika.util;

import com.forpleuvoir.suika.config.TooltipConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.Map;

import static com.forpleuvoir.suika.client.commands.TooltipCommand.OWNER;
import static com.forpleuvoir.suika.config.ConfigManager.ttConfig;

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
        if (ttConfig.isEnabled()) {
            String key = getKey(stack);
            Map<String, TooltipConfig.Data> map = ttConfig.getDatas();
            if (map.containsKey(key)) {
                TooltipConfig.Data data = ttConfig.getData(key);
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

    public static String getSkullOwner(ItemStack stack) {
        if (stack.getItem() == Items.PLAYER_HEAD && stack.hasTag()) {
            String string = null;
            CompoundTag compoundTag = stack.getTag();
            assert compoundTag != null;
            if (compoundTag.getString(OWNER).isEmpty()) {
                if (compoundTag.contains(OWNER, 8)) {
                    string = compoundTag.getString(OWNER);
                } else if (compoundTag.contains(OWNER, 10)) {
                    CompoundTag compoundTag2 = compoundTag.getCompound(OWNER);
                    if (compoundTag2.contains("Name", 8)) {
                        string = compoundTag2.getString("Name");
                    }
                }
                if (string != null) {
                    return string;
                }
            } else {
                return compoundTag.getString(OWNER);
            }
        }
        return "";
    }
}
