package io.github.anaxolotldreamerr.client.commands.cx.childcommand.resx;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.IDQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Unhate implements ECommand {
    private Cache<PlayerIdentifier> cache = PlayerType.getInstance().cache();
    private final Command<FabricClientCommandSource> COMMAND = context -> {
        if(!Cache.getInstance(PlayerType.filePath()).favoritesSet().stream().map(Favorite::id).collect(Collectors.toSet()).contains("hate")) {
            cache.addFavorites(new Favorite<>("hate", "hate", Set.of()));
        }
        String search;
        String object = context.getArgument("object",String.class);
        try{
            search = context.getArgument("search",String.class);
        }catch (Exception e){
            search = null;
        }
        if(search == null) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendCommand("favorites -p remove -i hate " + object);
            }
        }else {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendCommand("favorites -p remove -i hate "+search+" "+object);
            }
        }
        return 0;
    };
    private Unhate(){}
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("unhate").build());
        CommandNode<FabricClientCommandSource> remove = node.getChild("unhate");
        remove.addChild(ClientCommandManager
                .argument("search", SearchArgument.SearchTypeArgument.searchTypeArgument())
                .suggests(((context, builder) -> {
                    for(String arg : ArgumentFactory.getAllSearchName())
                        builder.suggest(arg);
                    return builder.buildFuture();
                })).then(ClientCommandManager
                        .argument("object", StringArgumentType.word())
                        .suggests(((context, builder) -> {
                            String input = context.getInput();
                            String[] args = input.split(" ");
                            String start = args[args.length-1];
                            Set<String> conform =new HashSet<>();
                            if(!input.endsWith(" ")) {
                                for (Identifier identifier : ArgumentFactory.searchArgument(context.getArgument("search",String.class)).getAll()) {
                                    if (identifier.name().toLowerCase().startsWith(start.toLowerCase())) {
                                        conform.add(identifier.name());
                                    }
                                }
                            }
                            if(conform.isEmpty()) {
                                for (Identifier identifier : ArgumentFactory.searchArgument(context.getArgument("search",String.class)).getAll()) {
                                    builder.suggest(identifier.name());
                                }
                            }else {
                                for (String name : conform){
                                    builder.suggest(name);
                                }
                            }
                            return builder.buildFuture();
                        })).executes(COMMAND)
                ).build());
        remove.addChild(ClientCommandManager
                .argument("object",StringArgumentType.word())
                .suggests(((context, builder) -> {
                    String input = context.getInput();
                    String[] args = input.split(" ");
                    String start = args[args.length-1];
                   Map<String,Favorite<Identifier>> map =ArgumentFactory
                            .queryArgument(IDQuery.getName())
                            .map(ArgumentFactory.typeArgument(PlayerType.name()).cache());
                   if(map.containsKey("hate")) {
                       Set<Identifier> identifiers = map.get("hate").objects();
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
                })).executes(COMMAND).build());
    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Unhate().register(node);
    }
}
