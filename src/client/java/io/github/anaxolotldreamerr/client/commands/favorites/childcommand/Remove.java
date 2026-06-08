package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.sun.jna.platform.win32.COM.Unknown;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.QueryArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.util.ArgumentUtil;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import io.github.anaxolotldreamerr.client.util.SearchUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Set;

/*
favorites <type> remove <query> [favorite] <search> [object]
favorites <type> remove <query> [favorite] [object]
favorites <type> remove [favorite] <search> [object]
favorites <type> remove [favorite] [object]
 */
public class Remove implements ECommand {
    private Favorite<Identifier> favorite;
    private Set<Identifier> objects;
    private Cache<Identifier> cache;
    private Remove(){}
    private Remove(Favorite<Identifier> favorite,Set<Identifier> objects,Cache<Identifier> cache){
        this.favorite = favorite;
        this.objects = objects;
        this.cache = cache;
    }
    private final Command<FabricClientCommandSource> COMMAND = context -> {
        ChatUtil.send(Component.literal("removing").withStyle(ChatFormatting.YELLOW));
        new Thread(()->{
            String stringType = context.getArgument("type",String.class);
            TypeArgument<Identifier> type = ArgumentFactory.typeArgument(stringType);
            QueryArgument query;
            SearchArgument<Identifier> search;
            Favorite<Identifier> favorite;
            Set<Identifier> identifiers;
            Identifier identifier;
            String object = context.getArgument("object",String.class);
            try {
                query = ArgumentFactory.queryArgument(context.getArgument("query",String.class));
            }catch (Exception e){
                query = ArgumentFactory.queryArgument(NameQuery.getName());
            }
            favorite = query.map(type.cache()).get(context.getArgument("favorite",String.class));
            try {
                search = ArgumentFactory.searchArgument(context.getArgument("search",String.class));
            }catch (Exception e){
                search = ArgumentFactory.defaultSearchArgument(stringType);
            }
            identifiers = favorite.objects();
            identifier = SearchUtil.search(search.getAll(),object);
            Set<Identifier> objects = search.filter(identifiers,identifier);
            try {
                String result = new Remove(favorite,objects,type.cache()).execute();
                ChatUtil.send(Component.literal(result).withStyle(ChatFormatting.GREEN));
            }catch (Exception e){
                ChatUtil.sendException(e);
            }
        }).start();

        return 0;
    };
    @Override
    public String execute() {
        try {
            Set<Identifier> removed = favorite.removeAll(objects,cache);
            return "remove "+removed.stream().map(Identifier::name)+" from "+favorite.name()+" successfully!A total of "+removed.size();
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("remove").build());
        CommandNode<FabricClientCommandSource> remove = node.getChild("remove");
        remove.addChild(QueryArgument.DEFAULT_QUERY.get().then(SearchArgument.SEARCH_WITH_DEFAULT_QUERY.apply(COMMAND)).build());
        remove.addChild(QueryArgument.DEFAULT_QUERY.get().then(SearchArgument.FROM_FAVORITE.get().executes(COMMAND)).build());
        remove.addChild(QueryArgument.QUERY.apply(ArgumentUtil.emptyCommand(),SearchArgument.SEARCH_WITH_QUERY.apply(COMMAND)).build());
        remove.addChild(QueryArgument.QUERY.apply(ArgumentUtil.emptyCommand(),SearchArgument.FROM_FAVORITE.get().executes(COMMAND)).build());

    }
    public static void load(CommandNode<FabricClientCommandSource> node){new Remove().register(node);}
}
