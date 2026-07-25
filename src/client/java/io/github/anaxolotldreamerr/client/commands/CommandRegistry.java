package io.github.anaxolotldreamerr.client.commands;


import io.github.anaxolotldreamerr.client.commands.cx.CNx;
import io.github.anaxolotldreamerr.client.commands.cx.CResx;
import io.github.anaxolotldreamerr.client.commands.cx.CTx;
import io.github.anaxolotldreamerr.client.commands.debuggingcommand.CUpdate;
import io.github.anaxolotldreamerr.client.commands.debuggingcommand.RenderDebugging;
import io.github.anaxolotldreamerr.client.commands.favorites.CFavorites;
import io.github.anaxolotldreamerr.client.commands.page.CPage;
import io.github.anaxolotldreamerr.client.commands.set.SetOption;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class CommandRegistry {
    private final static java.util.Set<EMCCommand> COMMANDS = java.util.Set.of(
            CFavorites.getInstance()
            , CUpdate.getInstance()
            , CPage.getInstance()
            , CNx.getInstance()
            , CResx.getInstance()
            , CTx.getInstance()
            , RenderDebugging.getInstance()
            ,new SetOption()
    );
    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> {
                    for(EMCCommand command : COMMANDS)command.register(dispatcher);
                }

        );
    }
}
