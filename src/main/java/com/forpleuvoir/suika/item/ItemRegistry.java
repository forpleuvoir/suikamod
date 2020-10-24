package com.forpleuvoir.suika.item;

import com.forpleuvoir.suika.Suika;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.forpleuvoir.suika.Suika.MOD_ID;
/**
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.item
 * @ClassName ItemRegistry
 * @author forpleuvoir
 * @CreateTime 2020/10/19 15:27
 * @Description item注册器
 */
public class ItemRegistry {
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "group"), () ->
            new ItemStack(ItemRegistry.IBUKI_GOURD)
    );
    public static final IbukiGourd IBUKI_GOURD = new IbukiGourd(new Item.Settings().group(GROUP).maxCount(64));

    public static void register() {
        Suika.LOGGER.info("suika mod items registry...");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "ibuki_gourd"), IBUKI_GOURD);


    }
}
