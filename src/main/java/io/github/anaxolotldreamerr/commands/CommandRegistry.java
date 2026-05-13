package io.github.anaxolotldreamerr.commands;


import io.github.anaxolotldreamerr.commands.favorites.CFavorites;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import java.util.Set;

public class CommandRegistry {
    private final static Set<EMCCommand> COMMANDS = Set.of(
            CFavorites.getInstance()
    );
    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> {
                    for(EMCCommand command : COMMANDS)command.register(dispatcher);
                }
        );
    }
}
