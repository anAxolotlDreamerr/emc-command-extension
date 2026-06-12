package io.github.anaxolotldreamerr.client.commands.cx;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CTx implements EMCCommand {
    private static  CTx INSTANCE = new CTx();
    private CTx(){}
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("tx"));
        CommandNode<FabricClientCommandSource> tx = dispatcher.register(ClientCommandManager.literal("tx"));
        return this;
    }
    public static CTx getInstance(){
        return INSTANCE;
    }
}
