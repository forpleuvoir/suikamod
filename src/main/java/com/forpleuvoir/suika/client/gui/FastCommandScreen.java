package com.forpleuvoir.suika.client.gui;

import com.forpleuvoir.suika.client.SuikaClient;
import com.forpleuvoir.suika.client.config.ConfigManager;
import com.forpleuvoir.suika.client.config.FastCommand;
import com.forpleuvoir.suika.client.util.Log;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
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

    private final Log log = new Log(FastCommandScreen.class);
    private final Map<String, String> datas;
    private Mode mode;


    public FastCommandScreen(Mode mode) {
        super(new LiteralText("Fast Command"));
        this.mode = mode;
        datas = ConfigManager.getFastCommand().getDatas();
    }

    @Override
    protected void init() {
        if (client != null) {
            if (!datas.isEmpty()) {
                int padding = 10;
                AtomicInteger indexX = new AtomicInteger(40);
                AtomicInteger indexY = new AtomicInteger(40);
                datas.forEach((k, v) -> {
                    int s = Math.max(FastCommand.getKeyLength(k) * 12, 20);
                    int x = indexX.get();
                    indexX.addAndGet(s);
                    int y = indexY.get();
                    if (this.width - indexX.get() <= 40 || indexX.get() + s > (this.width - 20)) {
                        indexY.addAndGet(20);
                        indexX.set(40);
                    }
                    this.addButton(new ButtonWidget(x + padding, y + padding, s, 20,
                            new LiteralText(k),
                            (button -> this.buttonClick(button, k, v)),
                            (button, matrices, mouseX, mouseY) -> {
                                if (client.cameraEntity != null) {
                                    String tip = "";
                                    switch (mode) {
                                        case RUN:
                                            tip = "点击执行指令";
                                            break;
                                        case EDIT:
                                            tip = "点击编辑";
                                    }
                                    List<Text> list = new ArrayList<>();
                                    list.add(new LiteralText(v).styled(style -> style.withColor(Formatting.RED)));
                                    list.add(new LiteralText(tip).styled(style -> style.withColor(Formatting.RED)));
                                    renderTooltip(matrices, list, mouseX, mouseY);
                                }
                            }));
                });
                boolean model = mode == Mode.EDIT;
                this.addButton(new ButtonWidget(this.width / 2 - 40, this.height - 50, 80, 20,
                        new LiteralText("编辑模式:" + model),
                        (button -> {
                            if (this.mode != Mode.RUN)
                                this.mode = Mode.RUN;
                            else {
                                this.mode = Mode.EDIT;
                            }
                            button.setMessage(new LiteralText("编辑模式:" + (mode == Mode.EDIT)));
                        })));
            }

        }
    }

    private void buttonClick(ButtonWidget button, String key, String value) {
        switch (mode) {
            case RUN:
                assert client != null;
                ((ClientPlayerEntity) Objects.requireNonNull(client.getCameraEntity()))
                        .networkHandler.sendPacket(new ChatMessageC2SPacket(value));
                this.onClose();
                break;
            case EDIT:
                openEditScreen(key, value);
        }
    }

    private void openEditScreen(String key, String value) {
        SuikaClient.openScreen(new EditScreen(key, value, this).getScreen());
    }

    private void changeMode(Mode mode) {
        this.mode = mode;
    }

    public static class EditScreen {
        private final String remark;
        private final String value;
        private final Screen parent;
        private String newRemark;
        private String newValue;

        protected EditScreen(String remark, String value, Screen parent) {
            this.parent = parent;
            this.remark = remark;
            this.value = value;
            this.newRemark = remark;
            this.newValue = value;
        }

        public Screen getScreen() {
            ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslatableText("快速指令编辑"));
            if (parent != null)
                builder.setParentScreen(parent);
            builder.setTransparentBackground(true);
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("快速指令编辑"));
            general.addEntry(entryBuilder.startStrField(new LiteralText("备注"), remark)
                    .setDefaultValue(remark)
                    .setSaveConsumer(s -> {
                        newRemark = s;
                        ConfigManager.getFastCommand().rename(remark, s);
                    })
                    .build());
            general.addEntry(entryBuilder.startStrField(new LiteralText("指令"), value)
                    .setDefaultValue(value)
                    .setSaveConsumer(s -> ConfigManager.getFastCommand().reset(remark, newRemark, s))
                    .build());
            return builder.build();
        }

    }


    public enum Mode {
        RUN,
        EDIT
    }
}
