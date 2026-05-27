package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

//favorites <type> delete [name]/-i [id]/-N [Name]
//    0        1      2      3    3   4   3    4
public class Delete implements ECommand {
    private final Command<FabricClientCommandSource> COMMAND = context ->{
        String[] args = context.getInput().split(" ");
        TypeArgument<Identifier> type = ArgumentFactory.typeArgument(args[1]);
        if(args.length<5) {
            type.cache().removeFavorites(new NameQuery().map(type.cache()).get(args[3]));
        }else {
            type.cache().removeFavorites(ArgumentFactory.queryArgument(args[3]).map(type.cache()).get(args[4]));
        }
        return 0;
    };
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
                                    suggestionsBuilder.suggest("-N");
                                    String[] args = context.getInput().split(" ");
                                    TypeArgument<Identifier> type = ArgumentFactory.typeArgument(args[1]);
                                    for(Favorite<Identifier> favorite : type.cache().favoritesSet()){
                                        suggestionsBuilder.suggest(favorite.name());
                                    }
                                    return suggestionsBuilder.buildFuture();
                                }).executes(COMMAND)
                                .then(
                                        ClientCommandManager
                                                .argument("name/id",StringArgumentType.word())
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
                                ).executes(COMMAND)
                        )
                        .build()
        );
    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Delete().register(node);
    }
}
