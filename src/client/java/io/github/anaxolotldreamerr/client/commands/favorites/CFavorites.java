package io.github.anaxolotldreamerr.client.commands.favorites;

import com.mojang.brigadier.CommandDispatcher;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CFavorites implements EMCCommand {
    private static final CFavorites INSTANCE = new CFavorites();
    private CFavorites(){};
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {

        return this;
    }
    public static CFavorites getInstance(){
        return INSTANCE;
    }
    private static enum ChildCommand{
        CREATE("create");
        private String command;
        ChildCommand(String command){
            this.command = command;
        }
    }
}
