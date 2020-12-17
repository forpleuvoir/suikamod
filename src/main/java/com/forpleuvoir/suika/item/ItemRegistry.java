package com.forpleuvoir.suika.item;

import com.forpleuvoir.chatbubbles.ReflectionUtils;
import com.forpleuvoir.suika.Suika;
import com.forpleuvoir.suika.item.weapon.Raikiri;
import com.forpleuvoir.suika.item.weapon.Ryuujinjakka;
import com.forpleuvoir.suika.item.weapon.RyuujinjakkaPlus;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.util.List;

import static com.forpleuvoir.suika.Suika.MOD_ID;

/**
 * item注册器
 *
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.item
 * @ClassName ItemRegistry
 * @CreateTime 2020/10/19 15:27
 * @Description item注册器
 */
public class ItemRegistry {
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "group"), () ->
            new ItemStack(ItemRegistry.IBUKI_GOURD)
    );
    public static final IbukiGourd IBUKI_GOURD = new IbukiGourd(settings().maxCount(64));
    public static final Ryuujinjakka RYUUJINJAKKA = new Ryuujinjakka(ToolMaterials.NETHERITE, 5, 1.0f, settings());
    public static final RyuujinjakkaPlus RYUUJINJAKKA_PLUS = new RyuujinjakkaPlus(ToolMaterials.NETHERITE, 10, 1.0f, settings());
    public static final Raikiri RAIKIRI = new Raikiri(settings());

    public static void register() {
        Suika.LOGGER.info("suika mod items registry...");
        Field[] items = ItemRegistry.class.getDeclaredFields();
        for (Field item : items) {
            item.setAccessible(true);
            List<Class<?>> list = ReflectionUtils.getSuperClass(item.getType());
            if (list.contains(Item.class)) {
                try {
                    register(item.getName().toLowerCase(), (Item) item.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void register(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), item);
    }

    private static Item.Settings settings() {
        return new Item.Settings().group(GROUP);
    }
}
