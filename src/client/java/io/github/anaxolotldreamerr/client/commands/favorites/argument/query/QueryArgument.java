package io.github.anaxolotldreamerr.client.commands.favorites.argument.query;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
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
                        String[] args = context.getInput().split(" ");
                        TypeArgument<Identifier> type = ArgumentFactory.typeArgument(args[1]);
                        for(Favorite<Identifier> favorite : type.cache().favoritesSet()){
                            suggestionsBuilder.suggest(favorite.name());
                        }
                        return suggestionsBuilder.buildFuture();
                    });
    BiFunction<Command<FabricClientCommandSource>,RequiredArgumentBuilder<FabricClientCommandSource,String>,Set<LiteralArgumentBuilder<FabricClientCommandSource>>> QUERY =(command,abuilder)-> {
        Set<LiteralArgumentBuilder<FabricClientCommandSource>> literals = new HashSet<>();
        RequiredArgumentBuilder<FabricClientCommandSource,String> required = ClientCommandManager
                .argument("favorite", StringArgumentType.word())
                .suggests(((context, builder) -> {
                    String[] args = context.getInput().split(" ");
                    for (String n : ArgumentFactory
                            .queryArgument(args[3])
                            .map(ArgumentFactory
                                    .typeArgument(args[1])
                                    .cache())
                            .keySet())
                        builder.suggest(n);
                    return builder.buildFuture();
                }))
                .executes(command)
                .then(abuilder);
        for(String arg : ArgumentFactory.getAllQueryName())
            literals.add(ClientCommandManager.literal(arg).then(required));
        return literals;
    };
    <T extends Identifier> Map<String, Favorite<T>> map(Cache<T> cache);
}
