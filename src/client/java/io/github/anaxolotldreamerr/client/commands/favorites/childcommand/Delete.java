package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//favorites <type> delete [name]/-i [id]/-N [Name]
//    0        1      2      3    3   4   3    4
public class Delete implements ECommand {
    private Delete(){}
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(
                ClientCommandManager
                        .literal("delete")
                        .then(ClientCommandManager
                                .argument("name/query", StringArgumentType.word())
                                .suggests((context,suggestionsBuilder)->{
                                    suggestionsBuilder.suggest("-i");
                                    suggestionsBuilder.suggest("-n");
                                    String[] args = context.getInput().split(" ");
                                    TypeArgument<Identifier> type = ArgumentFactory.typeArgument(args[1]);
                                    for(Favorite<Identifier> favorite : type.cache().favoritesSet()){
                                        suggestionsBuilder.suggest(favorite.name());
                                    }
                                    return suggestionsBuilder.buildFuture();
                                }).executes(context -> {
                                    String[] args = context.getInput().split(" ");
                                    TypeArgument<Identifier> type = ArgumentFactory.typeArgument(args[1]);
                                    type.cache().removeFavorites(new NameQuery().map(type.cache()).get(args[3]));
                                    return 0;
                                })
                        )
                        .build()
        );
    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Delete().register(node);
    }
}
