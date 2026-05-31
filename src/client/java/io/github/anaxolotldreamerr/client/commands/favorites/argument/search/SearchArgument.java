package io.github.anaxolotldreamerr.client.commands.favorites.argument.search;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.Argument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import javax.print.DocFlavor;
import java.util.Set;
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
                String args[] = context.getInput().split(" ");
                String type = args[1];
                for(Identifier identifier : ArgumentFactory.searchArgument(type).getAll()){
                    builder.suggest(identifier.name());
                }
                return builder.buildFuture();
            }));
    Supplier<RequiredArgumentBuilder<FabricClientCommandSource,String>> SEARCH_WITH_DEFAULT_QUERY =()-> ClientCommandManager
            .argument("search", StringArgumentType.word())
            .suggests(((context, builder) -> {
                for(String arg : ArgumentFactory.getAllSearchName())
                    builder.suggest(arg);
                return builder.buildFuture();
            })).then(ClientCommandManager
                    .argument("object",StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String args[] = context.getInput().split(" ");
                        for(Identifier identifier : ArgumentFactory.searchArgument(args[4]).getAll()){
                            builder.suggest(identifier.name());
                        }
                        return builder.buildFuture();
                    }))
            );
    Supplier<RequiredArgumentBuilder<FabricClientCommandSource,String>> SEARCH_WITH_QUERY =()-> ClientCommandManager
            .argument("search", StringArgumentType.word())
            .suggests(((context, builder) -> {
                for(String arg : ArgumentFactory.getAllSearchName())
                    builder.suggest(arg);
                return builder.buildFuture();
            })).then(ClientCommandManager
                    .argument("object",StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String args[] = context.getInput().split(" ");
                        for(Identifier identifier : ArgumentFactory.searchArgument(args[5]).getAll()){
                            builder.suggest(identifier.name());
                        }
                        return builder.buildFuture();
                    }))
            );
    Set<T> lookup(Identifier identifiers);
    Set<T> filter(Set<Identifier> identifiers);
    Set<T> getAll();
}
