package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.QueryArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.SearchArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import io.github.anaxolotldreamerr.client.util.ArgumentUtil;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import io.github.anaxolotldreamerr.client.util.QueryUntil;
import io.github.anaxolotldreamerr.client.util.SearchUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
favorites <type> add <query> [favorite] <search> [object] (7)
    0       1     2     3        4         5        6
favorites <type> add [favorite] [search] [object] (6)
    0       1     2     3          4        5
favorites <type> add <query> [favorite] [object] (6)
    0       1     2     3       4           5
favorites <type> add [favorite] [object] (5)
    0       1     2     3          4
*/
public class Add implements ECommand {
    private Favorite<Identifier> favorite;
    private Cache<Identifier> cache;
    private Set<Identifier> objects;
    private final Command<FabricClientCommandSource> COMMAND = (context -> {
            ChatUtil.send(Component.literal("adding").withStyle(ChatFormatting.YELLOW));
        new Thread(()->{
            String[] args = context.getInput().split(" ");
            TypeArgument<Identifier> type = ArgumentFactory.typeArgument(args[1]);
            Add add = new Add();
            QueryArgument query = ArgumentFactory.queryArgument(args[3]);
            add.cache = type.cache();
            Map<String,Favorite<Identifier>> map = query.map(add.cache);
            SearchArgument<Identifier> search;
            if(args.length == 5){
                add.favorite = QueryUntil.get(map,args[3]);
                search = ArgumentFactory.defaultSearchArgument(args[1]);
                add.objects = search.lookup(SearchUtil.search(search.getAll(),args[4]),type);
            }
            if (args.length == 6){
                if(!ArgumentFactory.getAllSearchName().contains(args[4])) {
                    add.favorite = QueryUntil.get(map,args[4]);
                    search = ArgumentFactory.defaultSearchArgument(args[1]);
                    add.objects = search.lookup(SearchUtil.search(search.getAll(), args[5]),type);
                }else {
                    add.favorite = QueryUntil.get(map,args[3]);
                    search = ArgumentFactory.searchArgument(args[4]);
                    add.objects = search.lookup(SearchUtil.search(search.getAll(),args[5]),type);
                }
            }
            if(args.length == 7){
                add.favorite = QueryUntil.get(map,args[4]);
                search = ArgumentFactory.searchArgument(args[5]);
                add.objects = search.lookup(SearchUtil.search(search.getAll(),args[6]),type);
            }
            try {
                String result = add.execute();
                ChatUtil.send(Component.literal(result).withStyle(ChatFormatting.GREEN));
            } catch (Exception e) {
                ChatUtil.sendException(e);
            }}).start();
        return 0;
    });
    private Add(){}
    @Override
    public String execute() {
        try {
            favorite.addAll(objects,cache);
            return "add "+objects.stream().map(Identifier::name).collect(Collectors.toSet())+" to "+favorite+" successfully!";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("add").build());
        CommandNode<FabricClientCommandSource> add = node.getChild("add");
        add.addChild(QueryArgument.DEFAULT_QUERY.get().then(SearchArgument.SEARCH_WITH_DEFAULT_QUERY.apply(COMMAND)).build());
        add.addChild(QueryArgument.DEFAULT_QUERY.get().then(SearchArgument.DEFAULT_SEARCH.get().executes(COMMAND)).build());
        for(LiteralArgumentBuilder<FabricClientCommandSource> liter : QueryArgument.QUERY.apply(ArgumentUtil.emptyCommand(),SearchArgument.SEARCH_WITH_QUERY.apply(COMMAND)))
            add.addChild(liter.build());
        for(LiteralArgumentBuilder<FabricClientCommandSource> liter : QueryArgument.QUERY.apply(ArgumentUtil.emptyCommand(),SearchArgument.DEFAULT_SEARCH.get().executes(COMMAND)))
            add.addChild(liter.build());


    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Add().register(node);
    }
}
