package com.forpleuvoir.suika.client;

import com.forpleuvoir.suika.client.config.ConfigManager;
import com.forpleuvoir.suika.client.config.HotKeys;
import com.forpleuvoir.suika.client.config.ModConfigApp;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author forpleuvoir
 * @BelongsProject suikamod
 * @BelonsPackge com.forpleuvoir.suika.client
 * @ClassName SuikaClient
 * @CreateTime 2020/10/19 11:44
 * @Description 客户端初始化类
 */
@Environment(EnvType.CLIENT)
public class SuikaClient implements ClientModInitializer {

    public static final String MOD_ID = "suika";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static final List<ClientTask> clientTasks = new ArrayList<>();
    private static final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        LOGGER.info("suika mod initializeClient...");
        ConfigManager.init();
        ModConfigApp.init();
        HotKeys.register();
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);

    }

    public void tick(MinecraftClient client) {
        HotKeys.tick(client);
        runTask(client);
    }

    private void runTask(MinecraftClient client) {
        Iterator<ClientTask> iterator = clientTasks.iterator();
        while (iterator.hasNext()) {
            ClientTask next = iterator.next();
            next.run(client);
            iterator.remove();
        }
    }

    public static void addTask(ClientTask clientTask) {
        clientTasks.add(clientTask);
    }

    public interface ClientTask {
        void run(MinecraftClient client);
    }

    public static void showInfo(Text text) {
        assert client.cameraEntity != null;
        client.inGameHud.addChatMessage(MessageType.GAME_INFO, text, client.cameraEntity.getUuid());
    }

    public static void showChat(Text text) {
        assert client.cameraEntity != null;
        client.inGameHud.addChatMessage(MessageType.CHAT, text, client.cameraEntity.getUuid());
    }

    public static void openScreen(Screen screen){
        clientTasks.add(client -> client.openScreen(screen));
    }
}
