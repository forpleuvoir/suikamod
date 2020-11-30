package com.forpleuvoir.suika.server.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.TranslatableText;

import java.util.Collection;
import java.util.Map;
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
    private static Set<String> warps;

    public WarpPointArgumentType(Map<String,?> map) {
        warps=map.keySet();
    }


    public static String getWarp(final CommandContext<?> context, final String name){
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readString();
        if (warps.contains(str)) {
            return str;
        } else {
            throw WARP_ERROR.create();
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String e : warps) {
            if(e.startsWith(builder.getRemaining()))
                builder.suggest(e);
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return warps;
    }
}
