package com.forpleuvoir.chatbubbles;

import net.minecraft.class_5599;
import net.minecraft.class_5617;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.chatbubbles
 * @className ChatBubbles
 * @createTime 2020/10/25 11:43
 */
@SuppressWarnings("ALL")
public class ChatBubbles {
    MinecraftClient game;
    boolean haveRenderManager = false;
    public static ChatBubbles instance;
    private ArrayList<String> newChatLines;
    private ArrayList<ChatBubbleMessage> messages;
    int maxLineLength = 30;
    boolean debug = false;
    public int MESSAGELIFETIME = 300;
    private String serverName = "";
    private TreeMap<String, ChatParseLine> customParseLines;
    private ChatParseLine customChatParseLine;
    private boolean voxelEnabled;
    public RenderPlayerChatBubbles renderPlayerChatBubbles;
    public RenderPlayerChatBubbles renderPlayerChatBubblesSlim;

    public ChatBubbles() {
        this.customParseLines = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.customChatParseLine = null;
        this.voxelEnabled = false;
        this.game = MinecraftClient.getInstance();
        instance = this;
        this.newChatLines = new ArrayList();
        this.messages = new ArrayList();
        this.loadCustomParseLines();
    }

    private void loadCustomParseLines() {
        this.customParseLines.put("mc.thevoxelbox.com", new ChatParseLine("^(?:[\\{\\[\\(<](\\w{2,16})[\\}\\]\\)>]:?|\\*?(\\w{2,16}):)(.*)", "1,2", 3));
        this.customParseLines.put("play.mc-sg.org", new ChatParseLine("^(?:<[^>]*>\\s*)?(\\w{2,16})\\s*>(.*)"));
        this.customParseLines.put("play.savagerealms.net", new ChatParseLine("^(?:\\[[^\\]]*\\]\\s*)*\\s*~?(\\w{2,16})\\s*(?:\\[[^\\]]*\\])?:\\s*(.*)"));
        this.customParseLines.put("rp.fr-minecraft.net", new ChatParseLine("^\\[(?:[^>]+>)?(\\w{2,16})\\|[^\\]]*\\]\\s*(.*)"));
        File settingsFile = new File(MinecraftClient.getInstance().runDirectory, "/mods/chatbubbles/customRegexes.txt");

        try {
            if (settingsFile.exists()) {
                BufferedReader in;
                String sCurrentLine;
                String[] curLine;
                ChatParseLine parseLine;
                for (in = new BufferedReader(new FileReader(settingsFile)); (sCurrentLine = in.readLine()) != null; this.customParseLines.put(curLine[0], parseLine)) {
                    curLine = sCurrentLine.split(" ");
                    parseLine = null;
                    if (curLine.length == 2) {
                        parseLine = new ChatParseLine(curLine[1], 1, 2);
                    } else if (curLine.length == 3) {
                        parseLine = new ChatParseLine(curLine[1], curLine[2]);
                    } else if (curLine.length == 4) {
                        parseLine = new ChatParseLine(curLine[1], curLine[2], Integer.parseInt(curLine[3]));
                    }
                }

                in.close();
            }
        } catch (Exception var11) {
            System.out.println("regex load error: " + var11.getLocalizedMessage());
        }

        try {
            if (!settingsFile.getParentFile().exists()) {
                settingsFile.getParentFile().mkdirs();
            }

            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));
            Set<Map.Entry<String, ChatParseLine>> lines = this.customParseLines.entrySet();
            Iterator iterator = lines.iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, ChatParseLine> entry = (Map.Entry) iterator.next();
                ChatParseLine line = (ChatParseLine) entry.getValue();
                String nameRefs = "";
                int[] nameRefsInt = line.getNameRefs();

                for (int t = 0; t < nameRefsInt.length - 1; ++t) {
                    nameRefs = nameRefs + nameRefsInt[t] + ",";
                }

                nameRefs = nameRefs + nameRefsInt[nameRefsInt.length - 1];
                out.println((String) entry.getKey() + " " + line.getRegex() + " " + nameRefs + " " + line.getTextRef());
            }

            out.close();
        } catch (Exception var10) {
            System.out.println("regex write error: " + var10.getLocalizedMessage());
        }

    }

    public boolean onTickInGame(MinecraftClient game) {
        if (!this.haveRenderManager) {
            this.loadRenderManager();
        }

        this.checkForChanges();
        if (!this.voxelEnabled) {
            synchronized (this.newChatLines) {
                Iterator var3 = this.newChatLines.iterator();

                while (var3.hasNext()) {
                    String newLine = (String) var3.next();
                    String[] authorText = this.parseLine(this.pare(this.scrubCodes(newLine)));
                    if (!authorText[0].equals("")) {
                        String[] messageLines = this.formatMessage(authorText[1]);
                         ChatBubbleMessage newMessage = new  ChatBubbleMessage(authorText[0], messageLines, this.game.inGameHud .getTicks ());
                        this.messages.add(0, newMessage);
                    }
                }

                this.newChatLines.clear();
            }
        }

        int currentTime = instance.game.inGameHud.getTicks();

        while (this.messages.size() > 0 && currentTime - (( ChatBubbleMessage) this.messages.get(this.messages.size() - 1)).getUpdatedCounter() >= this.MESSAGELIFETIME) {
            this.messages.remove(this.messages.size() - 1);
        }

        return true;
    }

    private void loadRenderManager() {
        System.out.println("getting renderer");
        EntityRenderDispatcher renderManager = MinecraftClient.getInstance().getEntityRenderDispatcher ();
        class_5617.class_5618 lv = new class_5617.class_5618(renderManager,
                (ItemRenderer)ReflectionUtils.getPrivateFieldValueByType(renderManager, EntityRenderDispatcher.class,ItemRenderer.class,1),
                MinecraftClient.getInstance().getResourceManager(),
                (class_5599)ReflectionUtils.getFieldValueByName(renderManager,"field_27760"),
                (TextRenderer)ReflectionUtils.getFieldValueByName(renderManager,"textRenderer"));
        if (renderManager == null) {
            System.out.println("failed to get render manager - chatbubbles");
        } else {
            Object skinMapObject = ReflectionUtils.getPrivateFieldValueByType(renderManager, EntityRenderDispatcher.class, Map.class, 1);
            if (skinMapObject == null) {
                System.out.println("could not get entityRenderMap chatbubbles");
            } else {
                this.renderPlayerChatBubbles = new  RenderPlayerChatBubbles(lv);
                this.renderPlayerChatBubblesSlim = new  RenderPlayerChatBubbles(lv, true);
                ((HashMap)skinMapObject).put("default", this.renderPlayerChatBubbles);
                ((HashMap)skinMapObject).put("slim", this.renderPlayerChatBubblesSlim);
                this.haveRenderManager = true;
            }
        }

    }

    private void checkForChanges() {
        String serverName = "";
        if (!this.game.isIntegratedServerRunning ()) {
            ServerInfo serverData = this.game.getCurrentServerEntry ();
            if (serverData != null) {
                serverName = serverData.address ;
            }

            if (serverName != null) {
                serverName = serverName.toLowerCase();
            }
        }

        if (!this.serverName.equals(serverName) && serverName != null && serverName != "") {
            this.serverName = serverName;
            this.loadCustomParseLine(serverName);
            this.voxelEnabled = false;
        }

    }

    private void loadCustomParseLine(String serverName) {
        this.customChatParseLine = (ChatParseLine)this.customParseLines.get(serverName);
    }

    public void clientString(String var1) {
        if (this.debug) {
            System.out.println("incoming message: " + var1);
        }

        if (!this.voxelEnabled) {
            synchronized(this.newChatLines) {
                this.newChatLines.add(var1);
            }
        }

    }

    private String pare(String string) {
        string = string.replaceAll("(.)\\1{6,}+", "$1$1$1$1$1$1");
        string = string.replaceAll("(..)\\1{6,}+", "$1$1$1$1$1$1");
        return string;
    }
    private String scrubCodes(String string) {
        string = string.replaceAll("(§.)", "");
        return string;
    }

    private String[] parseLine(String chatText) {
        String[] authorText = new String[]{"", ""};
        if (this.debug) {
            System.out.println(chatText);
        }

        Pattern pattern = Pattern.compile("^(?:\\[[^\\]]*\\]\\s*)*<(?:[^>]*[^>\\w])?(\\w{2,16})(?:\\s*\\([^\\)]*\\))?>+(.*)");
        if (this.debug) {
            System.out.println("check 1 A");
        }

        Matcher matcher = pattern.matcher(chatText);
        if (this.debug) {
            System.out.println("check 1 B");
        }

        if (matcher.find()) {
            if (this.debug) {
                System.out.println("check 1 C");
            }

            authorText[0] = matcher.group(1);
            authorText[1] = matcher.group(2);
            if (this.debug) {
                System.out.println("check 1 D");
            }
        } else {
            pattern = Pattern.compile("^(?:(?:\\[[^\\]]*\\]|(?:([^\\w\\s]?)([^\\w\\s])(?:(?!\\2).)+\\2\\1))\\s*)*(\\w{2,16})(?:\\s*\\([^\\)]*\\))?:(.*)");
            if (this.debug) {
                System.out.println("check 2 A");
            }

            matcher = pattern.matcher(chatText);
            if (this.debug) {
                System.out.println("check 2 B");
            }

            if (matcher.find()) {
                if (this.debug) {
                    System.out.println("check 2 C");
                }

                authorText[0] = matcher.group(3);
                authorText[1] = matcher.group(4);
                if (this.debug) {
                    System.out.println("check 2 D");
                }
            } else {
                pattern = Pattern.compile("^(?:(?:\\[[^\\]]*\\]|(?:([^\\w\\s]?)([^\\w\\s])(?:(?!\\2).)+.*\\2\\1))\\s*)*([\\W&&\\S])(\\w{2,16})\\3+(?:\\s*\\([^\\)]*\\))?:?(.*)");
                if (this.debug) {
                    System.out.println("check 3 A");
                }

                matcher = pattern.matcher(chatText);
                if (this.debug) {
                    System.out.println("check 3 B");
                }

                if (matcher.find()) {
                    if (this.debug) {
                        System.out.println("check 3 C");
                    }

                    authorText[0] = matcher.group(4);
                    authorText[1] = matcher.group(5);
                    if (this.debug) {
                        System.out.println("check 3 D");
                    }
                } else {
                    pattern = Pattern.compile("^([^\\*]?)[\\*]*\\w*\\s*(?:\\1\\[[^\\]]*\\])?[\\s]*(\\w{2,16})(?::|(?:\\s*>))(.*)");
                    if (this.debug) {
                        System.out.println("check 4 A");
                    }

                    matcher = pattern.matcher(chatText);
                    if (this.debug) {
                        System.out.println("check 4 B");
                    }

                    if (matcher.find()) {
                        if (this.debug) {
                            System.out.println("check 4 C");
                        }

                        authorText[0] = matcher.group(2);
                        authorText[1] = matcher.group(3);
                        if (this.debug) {
                            System.out.println("check 4 D");
                        }
                    } else {
                        pattern = Pattern.compile("(?:[^:]*[^:\\w])?(\\w{2,16})(?:\\s*\\([^\\)]*\\))?(?::|(?:\\s*>))(.*)");
                        if (this.debug) {
                            System.out.println("check 5 A");
                        }

                        matcher = pattern.matcher(chatText);
                        if (this.debug) {
                            System.out.println("check 5 B");
                        }

                        if (matcher.find()) {
                            if (this.debug) {
                                System.out.println("check 5 C");
                            }

                            authorText[0] = matcher.group(1);
                            authorText[1] = matcher.group(2);
                            if (this.debug) {
                                System.out.println("check 5 D");
                            }
                        }

                        if (this.debug) {
                            System.out.println("check 5 F");
                        }
                    }

                    if (this.debug) {
                        System.out.println("check 4 F");
                    }
                }

                if (this.debug) {
                    System.out.println("check 3 F");
                }
            }

            if (this.debug) {
                System.out.println("check 2 F");
            }
        }

        if (this.debug) {
            System.out.println("check 1 F");
        }

        if (this.customChatParseLine != null) {
            pattern = Pattern.compile(this.customChatParseLine.getRegex());
            if (this.debug) {
                System.out.println("check 0 A");
            }

            matcher = pattern.matcher(chatText);
            if (this.debug) {
                System.out.println("check 0 B");
            }

            if (matcher.find()) {
                if (this.debug) {
                    System.out.println("check 0 C");
                }

                int[] possibleAuthorRefs = this.customChatParseLine.getNameRefs();

                for(int t = 0; t < possibleAuthorRefs.length; ++t) {
                    String possibleAuthor = matcher.group(possibleAuthorRefs[t]);
                    if (possibleAuthor != null) {
                        authorText[0] = possibleAuthor;
                    }
                }

                authorText[1] = matcher.group(this.customChatParseLine.getTextRef());
                if (this.debug) {
                    System.out.println("check 0 D");
                }

                if (this.debug) {
                    System.out.println("author: " + authorText[0]);
                }
            }

            if (this.debug) {
                System.out.println("check 0 F");
            }
        }

        return authorText;
    }

    protected String[] formatMessage(String message) {
        StringTokenizer tokenizer = new StringTokenizer(message, " ");
        StringBuilder output = new StringBuilder(message.length());

        String word;
        for(int lineLen = 0; tokenizer.hasMoreTokens(); lineLen += word.length()) {
            word = tokenizer.nextToken();
            if (lineLen + word.length() > this.maxLineLength) {
                if (lineLen != 0) {
                    output.append("~break~");
                    lineLen = 0;
                }

                while(lineLen == 0 && word.length() > this.maxLineLength) {
                    output.append(word.substring(0, this.maxLineLength));
                    output.append("-~break~");
                    lineLen = 0;
                    word = word.substring(this.maxLineLength);
                }
            }

            if (lineLen != 0) {
                output.append(" ");
                ++lineLen;
            }

            output.append(word);
        }

        message = output.toString();
        return message.split("~break~");
    }

    public ArrayList<ChatBubbleMessage> getMessagesByAuthor(String author) {
        ArrayList<ChatBubbleMessage> relevantMessages = new ArrayList();
        Iterator var3 = this.messages.iterator();

        while (var3.hasNext()) {
            ChatBubbleMessage message = (ChatBubbleMessage) var3.next();
            if (message.getAuthor().equals(author)) {
                relevantMessages.add(message);
            }
        }
        return relevantMessages;
    }
}
