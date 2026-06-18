package io.github.anaxolotldreamerr.client.commands.cx;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.IDQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.QueryArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.HashSet;
import java.util.Set;

public enum CXArgument {
    DEFAULT {
        @Override
        public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String search) {
            return ClientCommandManager
                    .argument("object", StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length - 1];
                        Set<String> conform = new HashSet<>();
                        if (!input.endsWith(" ")) {
                            for (Identifier identifier : ArgumentFactory.searchArgument(search).getAll()) {
                                if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                    conform.add(identifier.name());
                                }
                            }
                        }
                        if (conform.isEmpty()) {
                            for (Identifier identifier : ArgumentFactory.searchArgument(search).getAll()) {
                                builder.suggest(identifier.name());
                            }
                        } else {
                            for (String name : conform) {
                                builder.suggest(name);
                            }
                        }
                        return builder.buildFuture();
                    }));
        }
    }, SEARCH {
        @Override
        public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(Command<FabricClientCommandSource> command) {
            return ClientCommandManager
                    .argument("search", SearchArgument.SearchTypeArgument.searchTypeArgument())
                    .suggests(((context, builder) -> {
                        for (String arg : ArgumentFactory.getAllSearchName())
                            builder.suggest(arg);
                        return builder.buildFuture();
                    })).then(ClientCommandManager
                            .argument("object", StringArgumentType.word())
                            .suggests(((context, builder) -> {
                                String input = context.getInput();
                                String[] args = input.split(" ");
                                String start = args[args.length - 1];
                                Set<String> conform = new HashSet<>();
                                if (!input.endsWith(" ")) {
                                    for (Identifier identifier : ArgumentFactory.searchArgument(context.getArgument("search", String.class)).getAll()) {
                                        if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                            conform.add(identifier.name());
                                        }
                                    }
                                }
                                if (conform.isEmpty()) {
                                    for (Identifier identifier : ArgumentFactory.searchArgument(context.getArgument("search", String.class)).getAll()) {
                                        builder.suggest(identifier.name());
                                    }
                                } else {
                                    for (String name : conform) {
                                        builder.suggest(name);
                                    }
                                }
                                return builder.buildFuture();
                            })).executes(command)
                    );
        }
    }, FROM_FAVORITE {
        @Override
        public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String type,String favorite) {
            return ClientCommandManager
                    .argument("object",StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length-1];
                        Set<Identifier> identifiers =ArgumentFactory
                                .queryArgument(IDQuery.getName())
                                .map(ArgumentFactory.typeArgument(type).cache())
                                .get(favorite)
                                .objects();
                        Set<String> conform =new HashSet<>();
                        if(!input.endsWith(" ")) {
                            for (Identifier identifier : identifiers) {
                                if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                    conform.add(identifier.name());
                                }
                            }
                        }
                        if(conform.isEmpty()) {
                            for (Identifier identifier : identifiers) {
                                builder.suggest(identifier.name());
                            }
                        }else {
                            for (String name : conform){
                                builder.suggest(name);
                            }
                        }
                        return builder.buildFuture();
                    }));
        }

        @Override
        public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String type) {
            return ClientCommandManager
                    .argument("object",StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length-1];
                        String query;
                        try {
                            query = context.getArgument("query",String.class);
                        }catch (Exception e){
                            query = NameQuery.getName();
                        }
                        Set<Identifier> identifiers =ArgumentFactory
                                .queryArgument(query)
                                .map(ArgumentFactory.typeArgument(type).cache())
                                .get(context.getArgument("favorite",String.class))
                                .objects();
                        Set<String> conform =new HashSet<>();
                        if(!input.endsWith(" ")) {
                            for (Identifier identifier : identifiers) {
                                if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                    conform.add(identifier.name());
                                }
                            }
                        }
                        if(conform.isEmpty()) {
                            for (Identifier identifier : identifiers) {
                                builder.suggest(identifier.name());
                            }
                        }else {
                            for (String name : conform){
                                builder.suggest(name);
                            }
                        }
                        return builder.buildFuture();
                    }));
        }
    },DEFAULT_QUERY{
        @Override
        public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String t) {
            return ClientCommandManager
                    .argument("favorite", StringArgumentType.word())
                    .suggests((context,suggestionsBuilder)->{
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length-1];
                        Set<String> confirm = new HashSet<>();
                        TypeArgument<Identifier> type = ArgumentFactory.typeArgument(t);
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
        }
    },QUERY{
        @Override
        public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String type, Command<FabricClientCommandSource> command, RequiredArgumentBuilder<FabricClientCommandSource, String> abuilder) {
                return ClientCommandManager.argument("query", QueryArgument.QueryTypeArgument.queryTypeArgument()).suggests((context, builder) -> {
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
                                    .queryArgument(context.getArgument("query",String.class))
                                    .map(ArgumentFactory
                                            .typeArgument(type)
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
    };

    public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(Command<FabricClientCommandSource> command){throw new UnsupportedOperationException(this.name()+" doesn't support the apply(Command<FabricClientCommandSource> command)");}

    public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String type){throw new UnsupportedOperationException(this.name()+" doesn't support the apply(String favorite)");}

    public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String type,String favorite){throw new UnsupportedOperationException(this.name()+" doesn't support the apply(String type,String favorite)");}

    public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String type,Command<FabricClientCommandSource> command,RequiredArgumentBuilder<FabricClientCommandSource, String> abuilder){throw new UnsupportedOperationException(this.name()+" doesn't support the apply(String type,Command<FabricClientCommandSource> command,RequiredArgumentBuilder<FabricClientCommandSource, String> builder)");}

}
