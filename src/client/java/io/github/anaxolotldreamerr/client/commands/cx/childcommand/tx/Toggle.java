package io.github.anaxolotldreamerr.client.commands.cx.childcommand.tx;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.config.Config;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.Set;

public class Toggle implements ECommand {
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        io.github.anaxolotldreamerr.client.commands.toggle.Toggle.add(node, Set.of(
                Config.BORDER_COLOR,
                Config.BORDER_OPACITY
        ));
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return 0;
    }
}
