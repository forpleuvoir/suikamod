package com.forpleuvoir.suika.client.gui;

import com.forpleuvoir.suika.config.ConfigManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.LiteralText;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * #package com.forpleuvoir.suika.client.gui
 * #class_name FastCommandScreen
 * #create_time 2021/3/10 14:13
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class FastCommandScreen extends Screen {

    private final Map<String, String> datas;


    public FastCommandScreen() {
        super(new LiteralText("Fast Command"));
        datas = ConfigManager.getFastCommand().getDatas();
    }

    @Override
    protected void init() {
        if (client != null)
            if (!datas.isEmpty()) {
                int padding = 10;
                AtomicInteger indexX = new AtomicInteger(40);
                AtomicInteger indexY = new AtomicInteger(40);
                datas.forEach((k, v) -> {
                    int s = k.length() * 12;
                    int x = indexX.get();
                    indexX.addAndGet(s);
                    int y = indexY.get();
                    if (this.width - indexX.get() <= 40 || indexX.get() + s > (this.width - 20)) {
                        indexY.addAndGet(20);
                        indexX.set(40);
                    }
                    this.addButton(new ButtonWidget(x + padding, y + padding, s, 20,
                            new LiteralText(k), (button -> {
                        ((ClientPlayerEntity) Objects.requireNonNull(client.getCameraEntity()))
                                .networkHandler.sendPacket(new ChatMessageC2SPacket(v));
                        this.onClose();
                    }), (button, matrices, mouseX, mouseY) -> {
                        if (client.cameraEntity != null)
                            client.inGameHud.addChatMessage(MessageType.GAME_INFO, new LiteralText(v), client.cameraEntity.getUuid());
                    }));
                });
            }
    }
}
