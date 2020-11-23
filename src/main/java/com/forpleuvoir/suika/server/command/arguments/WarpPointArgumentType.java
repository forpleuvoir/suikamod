package com.forpleuvoir.suika.server.command.arguments;

import com.forpleuvoir.suika.server.data.WarpPoint;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.TranslatableText;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.command.arguments
 * @class_name WarpPointArgymentType
 * @create_time 2020/11/23 12:06
 */

public class WarpPointArgumentType implements ArgumentType<String> {

    private static final SimpleCommandExceptionType WARP_ERROR = new SimpleCommandExceptionType(new TranslatableText("command.suika.exception.warp"));


    public static String getWarp(final CommandContext<?> context, final String name){
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readString();
        if (WarpPoint.warpPoints.containsKey(str)) {
            return str;
        } else {
            throw WARP_ERROR.create();
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String e : WarpPoint.warpPoints.keySet()) {
            if(e.startsWith(builder.getRemaining()))
                builder.suggest(e);
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return WarpPoint.warpPoints.keySet();
    }
}
