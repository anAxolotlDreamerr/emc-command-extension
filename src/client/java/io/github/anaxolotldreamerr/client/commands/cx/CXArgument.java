package io.github.anaxolotldreamerr.client.commands.cx;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.IDQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.HashSet;
import java.util.Map;
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
        public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String favorite) {
            return ClientCommandManager
                    .argument("object", StringArgumentType.word())
                    .suggests(((context, builder) -> {
                        String input = context.getInput();
                        String[] args = input.split(" ");
                        String start = args[args.length - 1];
                        Map<String, Favorite<Identifier>> map = ArgumentFactory
                                .queryArgument(IDQuery.getName())
                                .map(ArgumentFactory.typeArgument(PlayerType.name()).cache());
                        if (map.containsKey(favorite)) {
                            Set<Identifier> identifiers = map.get(favorite).objects();
                            Set<String> conform = new HashSet<>();
                            if (!input.endsWith(" ")) {
                                for (Identifier identifier : identifiers) {
                                    if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                        conform.add(identifier.name());
                                    }
                                }
                            }
                            if (conform.isEmpty()) {
                                for (Identifier identifier : identifiers) {
                                    builder.suggest(identifier.name());
                                }
                            } else {
                                for (String name : conform) {
                                    builder.suggest(name);
                                }
                            }
                        }
                        return builder.buildFuture();
                    }));
        }
    };

    public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(Command<FabricClientCommandSource> command){throw new UnsupportedOperationException(this.name()+" doesn't support the apply(Command<FabricClientCommandSource> command)");};

    public RequiredArgumentBuilder<FabricClientCommandSource, String> apply(String favorite){throw new UnsupportedOperationException(this.name()+" doesn't support the apply(String favorite)");};
}
