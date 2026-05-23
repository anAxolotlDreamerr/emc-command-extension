package io.github.anaxolotldreamerr.client.commands.favorites;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.childcommand.Create;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

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
                }).build());
        Create.register(commandNode.getChild("type"));
        return this;
    }
    public static CFavorites getInstance(){
        return INSTANCE;
    }

}
