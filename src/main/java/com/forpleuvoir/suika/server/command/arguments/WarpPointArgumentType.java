package com.forpleuvoir.suika.server.command.arguments;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.command.arguments
 * @class_name WarpPointArgumentType
 * @create_time 2020/11/23 12:06
 */

public class WarpPointArgumentType implements ArgumentType<String> {

    private static final SimpleCommandExceptionType WARP_ERROR = new SimpleCommandExceptionType(new TranslatableText("传送点不存在"));
    private static Set<String> EXAMPLES;

    public WarpPointArgumentType(Set<String> set) {
        EXAMPLES = set;
    }


    public static String getWarp(final CommandContext<?> context, final String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readString();
        if (EXAMPLES.contains(str)) {
            return str;
        } else {
            throw WARP_ERROR.create();
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String e : EXAMPLES) {
            if (e.startsWith(builder.getRemaining()))
                builder.suggest(e);
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Serializer implements ArgumentSerializer<WarpPointArgumentType> {
        public Serializer() {
        }

        public void toPacket(WarpPointArgumentType warpPointArgumentType, PacketByteBuf packetByteBuf) {
            Collection<String> collection = warpPointArgumentType.getExamples();
            String[] strings = collection.toArray(new String[0]);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < strings.length; i++) {
                stringBuilder.append(strings[i]);
                if (1 != strings.length - i) {
                    stringBuilder.append(",");
                }
            }
            byte[] bs=stringBuilder.toString().getBytes();
            packetByteBuf.writeByteArray(bs);
        }

        public WarpPointArgumentType fromPacket(PacketByteBuf packetByteBuf) {
            byte[] bs = packetByteBuf.readByteArray();
            String[] strings=new String(bs).split(",");
            Set<String> set= Sets.newHashSet();
            Collections.addAll(set, strings);
            return new WarpPointArgumentType(set);
        }

        public void toJson(WarpPointArgumentType warpPointArgumentType, JsonObject jsonObject) {

        }
    }
}
