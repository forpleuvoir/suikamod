package com.forpleuvoir.suika.client;

import com.forpleuvoir.suika.client.config.ConfigManager;
import com.forpleuvoir.suika.client.config.HotKeys;
import com.forpleuvoir.suika.client.config.ModConfigApp;
import com.forpleuvoir.suika.client.data.TimeTask;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    private static final List<ClientTask> clientTasks = new ArrayList<>();
    private static final List<TimeTask> timeTasks = new ArrayList<>();
    public static final MinecraftClient client = MinecraftClient.getInstance();
    private static long nowTime;

    @Override
    public void onInitializeClient() {
        Suika.LOGGER.info("suika mod initializeClient...");
        ModConfigApp.init();
        HotKeys.register();
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        ConfigManager.init();
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
        Iterator<TimeTask> iterator1=timeTasks.iterator();
        while (iterator1.hasNext()){
            TimeTask timeTask= iterator1.next();
            timeTask.run();
            if (timeTask.isRemoved()) {
                iterator1.remove();
            }
        }
    }

    public static void addTask(ClientTask clientTask) {
        clientTasks.add(clientTask);
    }

    public static void addTimeTask(TimeTask timeTask) {
        timeTasks.add(timeTask);
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

    public static void sendChatMessage(String message){
        ((ClientPlayerEntity) Objects.requireNonNull(client.getCameraEntity()))
                .networkHandler.sendPacket(new ChatMessageC2SPacket(message));
    }

    public static void openScreen(Screen screen) {
        clientTasks.add(client -> client.openScreen(screen));
    }
}
