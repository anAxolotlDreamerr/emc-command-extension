package io.github.anaxolotldreamerr.client.commands.cx;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.commands.cx.childcommand.resx.Hate;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CResx implements EMCCommand {
    private static  CResx INSTANCE = new CResx();
    private CResx(){}
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> resx = dispatcher.register(ClientCommandManager.literal("resx"));
        Hate.load(resx);
        return this;
    }
    public static CResx getInstance(){
        return INSTANCE;
    }
}
