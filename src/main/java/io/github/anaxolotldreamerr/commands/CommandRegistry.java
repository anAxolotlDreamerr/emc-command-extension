package io.github.anaxolotldreamerr.commands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandRegistry {
    public static void test(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("test_command").executes(context -> {
                context.getSource().sendSuccess(() -> Component.literal("Called /test_command."), false);
                return 1;
            }));
        });
    }
}
