package io.github.anaxolotldreamerr.client.commands.cx;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CNx implements EMCCommand {
    private static  CNx INSTANCE = new CNx();
    private CNx(){}
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> nx = dispatcher.register(ClientCommandManager.literal("nx"));
        return this;
    }
    public static CNx getInstance(){
        return INSTANCE;
    }
}
