package io.github.anaxolotldreamerr.commands.favorites;

import com.mojang.brigadier.CommandDispatcher;
import io.github.anaxolotldreamerr.commands.EMCCommand;
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
}
