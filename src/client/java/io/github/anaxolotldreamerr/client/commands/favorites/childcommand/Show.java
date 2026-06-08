package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.QueryArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.util.ArgumentUtil;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import io.github.anaxolotldreamerr.client.util.FavoritesUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
/*
    favorites <type> show <query> [favorite]
    favorites <type> show [favorite]
 */
public class Show implements ECommand {
    private Favorite<Identifier> favorite;
    private Show(){}
    private Show(Favorite<Identifier> favorite){
        this.favorite = favorite;
    }
    private final Command<FabricClientCommandSource> COMMAND = context -> {
        TypeArgument<Identifier> type = ArgumentFactory.typeArgument(context.getArgument("type",String.class));
        QueryArgument query;
        try{
            query = ArgumentFactory.queryArgument(context.getArgument("query",String.class));
        }catch (Exception e){
            query = ArgumentFactory.queryArgument(NameQuery.getName());
        }
        new Show(query.map(type.cache()).get(context.getArgument("favorite",String.class))).execute();
        return 0;
    };
    @Override
    public String execute() {
        FavoritesUtil.show(favorite);
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("show").build());
        node.getChild("show").addChild(QueryArgument.QUERY.apply(COMMAND, ArgumentUtil.emptyRequiredArgumentBuilder()).build());
        node.getChild("show").addChild(QueryArgument.DEFAULT_QUERY.get().executes(COMMAND).build());
    }
    public static void load(CommandNode<FabricClientCommandSource> node){new Show().register(node);}
}
