package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import io.github.anaxolotldreamerr.client.util.QueryUntil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

/*
favorites <type> delete [favorite] (4)
   0        1      2       3
favorites <type> delete <query> [favorite] (5)
   0        1      2       3        4
 */
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
            favorite = QueryUntil.get(new NameQuery().map(type.cache()),args[3]);
            type.cache().removeFavorites(favorite);
        }else {
            favorite =QueryUntil.get(ArgumentFactory.queryArgument(args[3]).map(type.cache()),args[4]);
            type.cache().removeFavorites(favorite);
        }
        return "Delete favorite:"+favorite.name()+" - "+favorite.id()+" successfully";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("delete").build());
        CommandNode<FabricClientCommandSource> delete = node.getChild("delete");
        delete.addChild(QueryArgument.DEFAULT_QUERY.get().executes(COMMAND).build());
        for(LiteralArgumentBuilder<FabricClientCommandSource> builder : QueryArgument.QUERY.apply(COMMAND, ArgumentUtil.emptyRequiredArgumentBuilder()))
            delete.addChild(builder.executes(COMMAND).build());

    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Delete().register(node);
    }
}
