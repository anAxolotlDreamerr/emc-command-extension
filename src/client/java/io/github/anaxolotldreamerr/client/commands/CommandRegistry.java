package io.github.anaxolotldreamerr.client.commands;


import io.github.anaxolotldreamerr.client.commands.debuggingcommand.CUpdate;
import io.github.anaxolotldreamerr.client.commands.favorites.CFavorites;
import io.github.anaxolotldreamerr.client.commands.page.CPage;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import java.util.Set;

public class CommandRegistry {
    private final static Set<EMCCommand> COMMANDS = Set.of(
            CFavorites.getInstance()
            , CUpdate.getInstance()
            , CPage.getInstance()
    );
    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> {
                    for(EMCCommand command : COMMANDS)command.register(dispatcher);
                }
        );
    }
}
