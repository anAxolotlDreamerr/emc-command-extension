package io.github.anaxolotldreamerr.client.commands.favorites.argument.query;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Obligations must be fulfilled for each new implementation class:
 * 1.Manually add the corresponding name field and its instance to query in ArgumentFactory
 */
public interface QueryArgument extends Argument {
    Supplier<RequiredArgumentBuilder<FabricClientCommandSource,String>> DEFAULT_QUERY =()-> ClientCommandManager
                    .argument("favorite", StringArgumentType.word())
                    .suggests((context,suggestionsBuilder)->{
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length-1];
                        Set<String> confirm = new HashSet<>();
                        TypeArgument<Identifier> type = ArgumentFactory.typeArgument(args[1]);
                        if(!input.endsWith(" ")){
                        for(Favorite<Identifier> favorite : type.cache().favoritesSet()){
                            if(favorite.name().startsWith(start)){
                                confirm.add(favorite.name());
                            }
                        }
                        }
                        if(confirm.isEmpty()){
                            for(Favorite<Identifier> favorite : type.cache().favoritesSet()){
                                confirm.add(favorite.name());
                            }
                        }
                        for(String favorite : confirm){
                            suggestionsBuilder.suggest(favorite);
                        }
                        return suggestionsBuilder.buildFuture();
                    });
    BiFunction<Command<FabricClientCommandSource>,RequiredArgumentBuilder<FabricClientCommandSource,String>,RequiredArgumentBuilder<FabricClientCommandSource,String>> QUERY =(command,abuilder)-> {
       return ClientCommandManager.argument("query",QueryTypeArgument.queryTypeArgument()).suggests((context, builder) -> {
            for(String query : ArgumentFactory.getAllQueryName()){
                builder.suggest(query);
            }
            return builder.buildFuture();
        }).then(ClientCommandManager
                .argument("favorite", StringArgumentType.word())
                .suggests(((context, builder) -> {
                    String input = context.getInput();
                    String[] args = input.split(" ");
                    String start = args[args.length-1];
                    Set<String> confirm = new HashSet<>();
                    Set<String> all = ArgumentFactory
                            .queryArgument(args[3])
                            .map(ArgumentFactory
                                    .typeArgument(args[1])
                                    .cache())
                            .keySet();
                    if(!input.endsWith(" ")) {
                        for (String f : all) {
                            if (f.startsWith(start)) {
                                confirm.add(f);
                            }
                        }
                    }
                    if(confirm.isEmpty()){
                        confirm.addAll(all);
                    }
                    for(String favorite : confirm){
                        builder.suggest(favorite);
                    }
                    return builder.buildFuture();
                }))
                .executes(command)
                .then(abuilder));
    };
    class QueryTypeArgument implements ArgumentType<String> {
        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String value = reader.readUnquotedString();
            if (!ArgumentFactory.getAllQueryName().contains(value)) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                        .literalIncorrect()
                        .create(value);
            }
            return value;
        }
        public static ArgumentType<String> queryTypeArgument(){
            return new QueryArgument.QueryTypeArgument();
        }
    }
    <T extends Identifier> Map<String, Favorite<T>> map(Cache<T> cache);
}
