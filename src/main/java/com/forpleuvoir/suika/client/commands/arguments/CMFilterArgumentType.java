package com.forpleuvoir.suika.client.commands.arguments;

import com.forpleuvoir.suika.client.config.ChatMessageFilter;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * 聊天过滤类型参数
 * #package com.forpleuvoir.suika.client.commands.arguments
 * #class_name CMFilterArgumentType
 * #create_time 2021/3/17 11:38
 * #project_name suikamod
 *
 * @author forpleuvoir
 */


public class CMFilterArgumentType implements ArgumentType<String> {

    private final Collection<String> EXAMPLES;

    public CMFilterArgumentType() {
        this.EXAMPLES = Lists.newArrayList();
        for (ChatMessageFilter.Type value : ChatMessageFilter.Type.values()) {
            EXAMPLES.add(value.getName());
        }
    }

    public static CMFilterArgumentType CMFType() {
        return new CMFilterArgumentType();
    }

    public static ChatMessageFilter.Type getType(final CommandContext<?> context, final String name) {
        return ChatMessageFilter.Type.byName(context.getArgument(name, String.class));
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readString().toLowerCase();
        if (EXAMPLES.contains(str)) {
            return str;
        }
        return null;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String s : EXAMPLES) {
            builder.suggest(s);
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
