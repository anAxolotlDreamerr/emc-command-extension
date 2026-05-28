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
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

//favorites <type> delete [name]/-i [id]/-N [Name]
//    0        1      2      3    3   4   3    4
public class Delete implements ECommand {
    private TypeArgument<Identifier> type;
    private String[] args;
    private final Command<FabricClientCommandSource> COMMAND = context ->{
        Delete delete = new Delete();
        delete.args = context.getInput().split(" ");
        delete.type = ArgumentFactory.typeArgument(delete.args[1]);
        try {
            String result = delete.execute();
            ChatUtil.send(Component.literal(result).withStyle(ChatFormatting.GREEN));
        }catch (Exception e){
            ChatUtil.sendException(e);
        }
        return 0;
    };
    private Delete(){}
    @Override
    public String execute() {
        Favorite<Identifier> favorite;
        if(args.length<5) {
            favorite = new NameQuery().map(type.cache()).get(args[3]);
            type.cache().removeFavorites(favorite);
        }else {
            favorite =ArgumentFactory.queryArgument(args[3]).map(type.cache()).get(args[4]);
            type.cache().removeFavorites(favorite);
        }
        return "Delete favorite:"+favorite.name()+" - "+favorite.id();
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
                                                })).executes(COMMAND)
                                )
                        )
                        .build()
        );
    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Delete().register(node);
    }
}
