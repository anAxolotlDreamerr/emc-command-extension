package io.github.anaxolotldreamerr.client.commands.favorites;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.commands.favorites.childcommand.Create;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.Arrays;
import java.util.Queue;

public class CFavorites implements EMCCommand {
    private static final CFavorites INSTANCE = new CFavorites();
    private CFavorites(){};
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> commandNode = dispatcher.register(LiteralArgumentBuilder.literal("favorites"));
        Create.register(commandNode);
        return this;
    }
    public static CFavorites getInstance(){
        return INSTANCE;
    }

}
