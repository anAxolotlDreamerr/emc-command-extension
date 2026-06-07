package io.github.anaxolotldreamerr.client.commands.favorites.argument.search;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Obligations must be fulfilled for each new implementation class:
 * 1.Manually add the corresponding name field and its instance to search in ArgumentFactory
 */
/*  favorites <type> <childCommand> <query> [favorite] <search> [object]
        0       1           2           3       4          5        6
    favorites <type> <childCommand> <favorite> <search> [object]
        0       1           2           3       4          5
    favorites <type> <childCommand> <query> [favorite] [object]     |
        0       1           2           3       4          5        | --> DEFAULT_SEARCH
    favorites <type> <childCommand> <favorite> [object]             |
        0       1           2           3       4
*/
public interface SearchArgument<T extends Identifier> extends Argument {
    Supplier<RequiredArgumentBuilder<FabricClientCommandSource,String>> DEFAULT_SEARCH =()-> ClientCommandManager
            .argument("object",StringArgumentType.word())
            .suggests(((context, builder) -> {
                String input = context.getInput();
                String[] args = input.split(" ");
                String type = args[1];
                String start = args[args.length-1];
                Set<String> conform =new HashSet<>();
                if(!input.endsWith(" ")) {
                    for (Identifier identifier : ArgumentFactory.searchArgument(type).getAll()) {
                        if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                            conform.add(identifier.name());
                        }
                    }
                }
                if(conform.isEmpty()) {
                    for (Identifier identifier : ArgumentFactory.searchArgument(type).getAll()) {
                        builder.suggest(identifier.name());
                    }
                }else {
                    for (String name : conform){
                        builder.suggest(name);
                    }
                }
                return builder.buildFuture();
            }));
    Function<Command<FabricClientCommandSource>,RequiredArgumentBuilder<FabricClientCommandSource,String>> SEARCH_WITH_DEFAULT_QUERY =(command)-> ClientCommandManager
            .argument("search", SearchTypeArgument.searchTypeArgument())
            .suggests(((context, builder) -> {
                for(String arg : ArgumentFactory.getAllSearchName())
                    builder.suggest(arg);
                return builder.buildFuture();
            })).then(ClientCommandManager
                    .argument("object",StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length-1];
                        Set<String> conform =new HashSet<>();
                        if(!input.endsWith(" ")) {
                            for (Identifier identifier : ArgumentFactory.searchArgument(args[4]).getAll()) {
                                if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                    conform.add(identifier.name());
                                }
                            }
                        }
                        if(conform.isEmpty()) {
                            for (Identifier identifier : ArgumentFactory.searchArgument(args[4]).getAll()) {
                                builder.suggest(identifier.name());
                            }
                        }else {
                            for (String name : conform){
                                builder.suggest(name);
                            }
                        }
                        return builder.buildFuture();
                    })).executes(command)
            );
    Function<Command<FabricClientCommandSource>,RequiredArgumentBuilder<FabricClientCommandSource,String>> SEARCH_WITH_QUERY =(command)-> ClientCommandManager
            .argument("search", SearchTypeArgument.searchTypeArgument())
            .suggests(((context, builder) -> {
                for(String arg : ArgumentFactory.getAllSearchName())
                    builder.suggest(arg);
                return builder.buildFuture();
            })).then(ClientCommandManager
                    .argument("object",StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length-1];
                        Set<String> conform =new HashSet<>();
                        if(!input.endsWith(" ")) {
                            for (Identifier identifier : ArgumentFactory.searchArgument(args[5]).getAll()) {
                                if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                    conform.add(identifier.name());
                                }
                            }
                        }
                        if(conform.isEmpty()) {
                            for (Identifier identifier : ArgumentFactory.searchArgument(args[5]).getAll()) {
                                    builder.suggest(identifier.name());
                            }
                        }else {
                            for (String name : conform){
                                builder.suggest(name);
                            }
                        }
                        return builder.buildFuture();
                    })).executes(command)
            );
    Set<Identifier> lookup(T identifier, TypeArgument<? extends Identifier> type);
    Set<Identifier> filter(Set<T> identifiers,TypeArgument<? extends Identifier> type);
    Set<T> getAll();
    class SearchTypeArgument implements ArgumentType<String> {
        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            String value = reader.readUnquotedString();
            if (!ArgumentFactory.getAllSearchName().contains(value)) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                        .literalIncorrect()
                        .create(value);
            }
            return value;
        }
        public static ArgumentType<String> searchTypeArgument(){
            return new SearchTypeArgument();
        }
    }
}
