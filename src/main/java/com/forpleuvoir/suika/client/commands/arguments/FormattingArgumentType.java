package com.forpleuvoir.suika.client.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @BelongsProject suikamod
 * @BelongsPackage com.forpleuvoir.suika.client.commands.arguments
 * @ClassName FormattingArgumentType
 * @author forpleuvoir
 * @CreateTime 2020/10/19 21:10
 * @Description 格式指令参数
 */
public class FormattingArgumentType implements ArgumentType<String> {
    public static final SimpleCommandExceptionType FORMATTING_ERROR = new SimpleCommandExceptionType(new TranslatableText("argument.suika.formatting.error"));
    private static final String NONE = "none";
    private Set<String> set;
    private Collection<String> examples;

    public FormattingArgumentType(){
        Set<String> set=new HashSet<>();
        for (Formatting e : Formatting.values()) {
            set.add(e.getName());
        }
        this.set=set;
        examples = Lists.newArrayList();
        for (Formatting e : Formatting.values()) {
            if (!("reset".equals(e.getName()))) {
                examples.add(e.getName());
            }
        }
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readString().toLowerCase();
        if (str.equals(NONE)) {
            return "reset";
        }
        if(set.contains(str)){
            return str;
        }else{
            throw FORMATTING_ERROR.create();
        }
    }

    public static FormattingArgumentType formatting() {
        return new FormattingArgumentType();
    }

    public static Formatting getFormatting(final CommandContext<?> context, final String name) {
        return Formatting.byName(context.getArgument(name, String.class));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Formatting e : Formatting.values()) {
            if (!("reset".equals(e.getName()))
                    && e.getName().startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(e.getName());
            }
        }
        if (NONE.startsWith(builder.getRemaining().toLowerCase())) {
            builder.suggest(NONE);
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
