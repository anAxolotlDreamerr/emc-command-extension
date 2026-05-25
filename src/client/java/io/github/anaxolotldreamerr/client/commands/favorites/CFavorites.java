package io.github.anaxolotldreamerr.client.commands.favorites;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.childcommand.Create;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//favorites <type>
public class CFavorites implements EMCCommand {
    private static final CFavorites INSTANCE = new CFavorites();
    private CFavorites(){};
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> commandNode = dispatcher.register(LiteralArgumentBuilder.literal("favorites"));
        commandNode.addChild(ClientCommandManager
                .argument("type", StringArgumentType.word())
                .suggests((context,suggestionsBuilder)->{
                    for(String name : ArgumentFactory.getAllTypeName()){
                        suggestionsBuilder.suggest(name);
                    }
                    return suggestionsBuilder.buildFuture();
                }).executes((context -> {
                    String type = context.getInput().split(" ")[1];
                    TypeArgument<Identifier> arg = ArgumentFactory.typeArgument(type);
                    ChatUtil.showFavoriteList(arg.cache());
                    return 0;
                })).build());
        Create.register(commandNode.getChild("type"));
        return this;
    }
    public static CFavorites getInstance(){
        return INSTANCE;
    }

}
